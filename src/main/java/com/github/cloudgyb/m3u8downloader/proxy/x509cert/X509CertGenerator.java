package com.github.cloudgyb.m3u8downloader.proxy.x509cert;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Stream;

public class X509CertGenerator {
    private static final Certificate CACert;
    private static final PrivateKey CAPrivateKey;

    static {
        Security.addProvider(new BouncyCastleProvider());
        StringBuilder sb = new StringBuilder();

        try (InputStream resourceAsStream = X509CertGenerator.class
                .getResourceAsStream("/CACert.pem")) {
            assert resourceAsStream != null;
            try (InputStreamReader fileReader = new InputStreamReader(resourceAsStream)) {
                LineNumberReader r = new LineNumberReader(fileReader);
                Stream<String> lines = r.lines();
                lines.forEach(l -> {
                    if (!l.startsWith("-----") && !l.isBlank()) {
                        sb.append(l.trim());
                    }
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        byte[] certBytes = Base64.getDecoder().decode(sb.toString());
        X509CertificateHolder x509CertificateHolder;
        try {
            x509CertificateHolder = new X509CertificateHolder(certBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CACert = x509CertificateHolder.toASN1Structure();
        sb.delete(0, sb.length());
        try (InputStream resourceAsStream = X509CertGenerator.class
                .getResourceAsStream("/CAPrivate.key")) {
            assert resourceAsStream != null;
            try (LineNumberReader r = new LineNumberReader(new InputStreamReader(resourceAsStream))) {
                Stream<String> lines = r.lines();
                lines.forEach(l -> {
                    if (!l.startsWith("-----") && !l.isBlank()) {
                        sb.append(l.trim());
                    }
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        PKCS8EncodedKeySpec x509EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(sb.toString()));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            CAPrivateKey = keyFactory.generatePrivate(x509EncodedKeySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] generateProxyServerHostX509Cert(String domainOrIp) throws IOException, OperatorCreationException {
        StringBuilder certPriKeyStr = new StringBuilder();
        StringBuilder certStr = new StringBuilder();
        generateX509V3Cert(domainOrIp, CACert, CAPrivateKey, certPriKeyStr, certStr);
        return new String[]{certPriKeyStr.toString(), certStr.toString()};
    }

    /**
     * 根据 CA 证书生成（签发）子证书
     *
     * @param domainOrIp   域名或 ip
     * @param CACert       CA 根证书
     * @param CAPrivateKey CA 根证书的私有密钥，用于对签发证书签名
     * @param privateKey   出参，生成的证书的 pem 格式的私有 key
     * @param x509Cert     出参，生成的 pem 格式的证书
     */
    public static void generateX509V3Cert(String domainOrIp, Certificate CACert, PrivateKey CAPrivateKey,
                                          StringBuilder privateKey, StringBuilder x509Cert)
            throws OperatorCreationException, IOException {
        X500Name subject = generateSubject("CN", "Beijing", "Beijing", null, null, domainOrIp);
        //生成证书 RSA 密钥对
        KeyPair keyPair = generateRsaKeyPair(2048);
        String[] generate = generate(subject, CACert.getSubject(), keyPair, CAPrivateKey);
        privateKey.append(generate[0]);
        x509Cert.append(generate[1]);
    }

    /**
     * 生成指定域名或 ip 的 x509v3 证书
     *
     * @param domainOrIp 域名或 ip，如果是 CA 证书，可以是 CA 机构名称
     * @param privateKey 出参，生成的证书的 pem 格式的私有 key
     * @param x509Cert   出参，生成的 pem 格式的证书
     */
    public static void generateSelfSignedX509V3Cert(String domainOrIp, StringBuilder privateKey, StringBuilder x509Cert)
            throws OperatorCreationException, IOException {
        //生成主题信息
        X500Name subject = generateSubject("CN", "Beijing", "Beijing", "Proxy CA", "Proxy CA", domainOrIp);
        //生成RSA密钥对
        KeyPair keyPair = generateRsaKeyPair(2048);
        String[] generate = generate(subject, subject, keyPair, keyPair.getPrivate());
        privateKey.append(generate[0]);
        x509Cert.append(generate[1]);
    }

    private static String[] generate(X500Name subject, X500Name issuer, KeyPair keyPair, PrivateKey caPrivateKey)
            throws OperatorCreationException, IOException {
        //下面是私钥key生成的过程
        byte[] privateKeyEncode = keyPair.getPrivate().getEncoded();
        String privateKeyStr = Base64.getEncoder().encodeToString(privateKeyEncode);
        String privateKeyFileContent =
                """
                        -----BEGIN PRIVATE KEY-----
                        %s
                        -----END PRIVATE KEY-----
                        """.formatted(lf(privateKeyStr, 64));
        //下面是PEM格式的证书生成过程
        long currTimestamp = System.currentTimeMillis();
        X509v3CertificateBuilder x509v3CertificateBuilder = new JcaX509v3CertificateBuilder(
                issuer, BigInteger.valueOf(currTimestamp),
                new Date(currTimestamp), new Date(currTimestamp + (long) 365 * 24 * 60 * 60 * 1000),
                subject, keyPair.getPublic());
        RDN[] rdNs = subject.getRDNs(BCStyle.CN);
        if (rdNs != null && rdNs.length > 0) {
            ASN1Encodable value = rdNs[0].getFirst().getValue();
            GeneralName[] subjectAltNames = new GeneralName[]{
                    new GeneralName(GeneralName.dNSName, value),
            };

            Extension extension = Extension.create(Extension.subjectAlternativeName, false,
                    new GeneralNames(subjectAltNames));
            x509v3CertificateBuilder.addExtension(extension);
        }

        JcaContentSignerBuilder sha256WITHRSA = new JcaContentSignerBuilder("SHA256WITHRSA");
        ContentSigner contentSigner = sha256WITHRSA.build(caPrivateKey);
        X509CertificateHolder x509CertificateHolder = x509v3CertificateBuilder.build(contentSigner);
        Certificate certificate = x509CertificateHolder.toASN1Structure();
        byte[] encoded = certificate.getEncoded();
        String certStr = Base64.getEncoder().encodeToString(encoded);
        String certFileContent =
                """
                        -----BEGIN CERTIFICATE-----
                        %s
                        -----END CERTIFICATE-----
                        """.formatted(lf(certStr, 64));
        return new String[]{privateKeyFileContent, certFileContent};
    }

    /**
     * 生成Subject信息
     *
     * @param C  Country Name (国家代号),eg: CN
     * @param ST State or Province Name (洲或者省份),eg: Beijing
     * @param L  Locality Name (城市名),eg: Beijing
     * @param O  Organization Name (可以是公司名称),eg: 北京创新乐知网络技术有限公司
     * @param OU Organizational Unit Name (可以是单位部门名称)
     * @param CN Common Name (服务器ip或者域名),eg: 192.168.30.71 or www.baidu.com
     * @return X500Name Subject
     */
    public static X500Name generateSubject(String C, String ST, String L,
                                           String O, String OU, String CN) {
        X500NameBuilder x500NameBuilder = new X500NameBuilder();
        if (C != null) {
            x500NameBuilder.addRDN(BCStyle.C, C);
        }
        if (ST != null) {
            x500NameBuilder.addRDN(BCStyle.ST, ST);
        }
        if (L != null) {
            x500NameBuilder.addRDN(BCStyle.L, L);
        }
        if (O != null) {
            x500NameBuilder.addRDN(BCStyle.O, O);
        }
        if (OU != null) {
            x500NameBuilder.addRDN(BCStyle.OU, OU);
        }
        if (CN != null) {
            x500NameBuilder.addRDN(BCStyle.CN, CN);
        }
        return x500NameBuilder.build();
    }

    public static KeyPair generateRsaKeyPair(int keySize) {
        try {
            KeyPairGenerator rsa = KeyPairGenerator.getInstance("RSA");
            rsa.initialize(keySize);
            return rsa.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String lf(String str, int lineLength) {
        assert str != null;
        assert lineLength > 0;
        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        int n = 0;
        for (char aChar : chars) {
            sb.append(aChar);
            n++;
            if (n == lineLength) {
                n = 0;
                sb.append("\n");
            }
        }
        return sb.toString();
    }

}
