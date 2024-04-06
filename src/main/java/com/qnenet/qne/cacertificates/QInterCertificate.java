package com.qnenet.qne.cacertificates;

import com.qnenet.qne.system.constants.QSysConstants;
import com.qnenet.qne.system.impl.QNEPaths;
import com.qnenet.qne.system.utils.QDateTimeUtils;
import com.qnenet.qne.system.utils.QSecurityUtils;
import com.qnenet.qne.system.utils.QStringUtils;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/*
 * Note :
 * PKCS12 keystore requires keystore Pwd and
 * private key password to be the same
 * */

@Component
@Scope("prototype")
public class QInterCertificate {

    private static final int VALIDITY_YEARS = 10;
    private static final String INTERCERT_NAME_PREFIX = "QNEIntercert-";
    private static final String INTERCERT_ALIAS_PREFIX = "qneintercert-";

    private String nameCurve = "P-256";
    private String keyAlgo = "ECDSA";
    private String signatureAlgo = "SHA256withECDSA";
    private String bcProvider = "BC";

//    static Logger log = LoggerFactory.getLogger(QInterCertificate.class);

////////////  Custom  //////////////////////////////////////////////////////////////////////////////////////////////////


    //    @Autowired
    QNEPaths qnePaths;

    private PublicKey caPublicKey;
    private PrivateKey caPrivateKey;

    private char[] interKeyStorePassPhrase;
    private X509Certificate rootCertificate;
    private String interName;
    private String interalias;
    private KeyStore rootKeyStore;
    private char[] rootKeyStorePassPhrase;
    private KeyStore interKeyStore;
    private KeyPair interKeyPair;

///////////////////////////////////////////////////////////////////////////////////////////////////
/////// Activate //////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    /*
     *
     *
     *
     * Creates a new Inter Certificate every time it is activated
     *
     *
     *
     */

//    public QInterCertificate(int count, QNEPaths qnePaths) {
//        this.count = count;
//    }

    public void init(QNEPaths qnePaths, int count) {
        Path interKeyStorePath = Paths.get(qnePaths.getRepositoryPath().toString(), QSysConstants.INTER_KEYSTORE_DIR_NAME);
        Path interKeyStoreFilePath = Paths.get(interKeyStorePath.toString(), "interKeyStore.p12");
        if (Files.exists(interKeyStoreFilePath))
            return;

        try {
            JFrame jFrame = new JFrame();
            rootKeyStorePassPhrase = JOptionPane.showInputDialog(jFrame, "Enter Root Keystore Pass Phrase").toCharArray();
            interKeyStorePassPhrase = JOptionPane.showInputDialog(jFrame, "Enter Inter Keystore Pass Phrase").toCharArray();

            Path rootKeyStorePath = Paths.get(qnePaths.getRepositoryPath().toString(), QSysConstants.ROOT_KEYSTORE_DIR_NAME);
            Path rootKeyStoreFilePath = Paths.get(rootKeyStorePath.toString(), "rootKeyStore.p12");

            rootKeyStore = QSecurityUtils.loadKeyStore(rootKeyStoreFilePath, rootKeyStorePassPhrase);
            KeyStore.PrivateKeyEntry rootEntry = (KeyStore.PrivateKeyEntry) rootKeyStore.getEntry("qnerootca",
                    new KeyStore.PasswordProtection(rootKeyStorePassPhrase));
            caPrivateKey = rootEntry.getPrivateKey();
            rootCertificate = (X509Certificate) rootEntry.getCertificate();
            caPublicKey = rootCertificate.getPublicKey();


            if (Files.notExists(interKeyStorePath)) {
                Files.createDirectories(interKeyStorePath);
                interKeyStore = QSecurityUtils.createKeyStore();
                QSecurityUtils.saveKeyStore(interKeyStoreFilePath, interKeyStore, interKeyStorePassPhrase);
            } else {
                interKeyStore = QSecurityUtils.loadKeyStore(interKeyStoreFilePath, interKeyStorePassPhrase);
            }

            for (int i = 0; i < count; i++) {

                createInterCert();

            }

            QSecurityUtils.saveKeyStore(interKeyStoreFilePath, interKeyStore, interKeyStorePassPhrase);

        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (UnrecoverableEntryException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (OperatorCreationException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

//        log.info("Hello from -> " + getClass().getSimpleName());
    }

    private void createInterCert() throws KeyStoreException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CertIOException, CertificateException, OperatorCreationException, SignatureException, InvalidKeyException {
        ArrayList<String> allAliases = Collections.list(interKeyStore.aliases());
        ArrayList<String> interAliasses = new ArrayList<>();
        for (String alias : allAliases) {
            if (alias.startsWith(INTERCERT_ALIAS_PREFIX)) {
                interAliasses.add(alias);
            }
        }
        Collections.sort(interAliasses);

        if (interAliasses.isEmpty()) {
            interalias = INTERCERT_ALIAS_PREFIX + "001";
            interName = INTERCERT_NAME_PREFIX + "001";
        } else {
            String last = interAliasses.get(interAliasses.size() - 1);
            String[] split = last.split("-");
            int nextId = Integer.valueOf(split[1]) + 1;
            interalias = INTERCERT_ALIAS_PREFIX + QStringUtils.int3(nextId);
            interName = INTERCERT_NAME_PREFIX + QStringUtils.int3(nextId);
        }

///////////////////////////////////////////////


        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(nameCurve);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyAlgo, "BC");
        keyPairGenerator.initialize(ecSpec, new SecureRandom());
        interKeyPair = keyPairGenerator.generateKeyPair();

        X509Certificate intermediateCert = createIntermediateCert();

        X509Certificate[] certificateArray = new X509Certificate[]{intermediateCert, rootCertificate};

        interKeyStore.setKeyEntry(interalias, interKeyPair.getPrivate(), interKeyStorePassPhrase, certificateArray);
    }

    //    @Deactivate
    public void deactivate() {
        Arrays.fill(interKeyStorePassPhrase, '0');
//        log.info("Goodbye from -> " + getClass().getSimpleName());
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
/////// Inter Certificate /////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    public X509Certificate createIntermediateCert() throws NoSuchAlgorithmException, CertIOException, CertificateException, OperatorCreationException, SignatureException, InvalidKeyException, NoSuchProviderException {

        PublicKey interPubKey = interKeyPair.getPublic();

        String subject = "C=AU, O=QNECommunity, OU=QNE " + interName;

        X500Name interCertSubject = new X500Name(subject);

        LocalDate localNow = LocalDate.now();
        LocalDate localNowMinus = localNow.minus((long) 7, ChronoUnit.DAYS);

        Date dateFrom = Date.from(localNowMinus.atStartOfDay().toInstant(ZoneOffset.UTC));
        Date dateTo = QDateTimeUtils.nowUTCPlusYears(VALIDITY_YEARS);

        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());

        ContentSigner interCertContentSigner = new JcaContentSignerBuilder(signatureAlgo).setProvider(bcProvider).build(caPrivateKey);
        X509v3CertificateBuilder interCertBuilder = new JcaX509v3CertificateBuilder(rootCertificate, serialNumber, dateFrom, dateTo, interCertSubject, interKeyPair.getPublic());

        JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

        interCertBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(0));
        interCertBuilder.addExtension(Extension.authorityKeyIdentifier, false, extUtils.createAuthorityKeyIdentifier(rootCertificate));
        interCertBuilder.addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(interPubKey));


        // Create a cert holder and export to X509Certificate
        X509CertificateHolder interCertHolder = interCertBuilder.build(interCertContentSigner);
        X509Certificate interCert = new JcaX509CertificateConverter().setProvider(bcProvider).getCertificate(interCertHolder);

        interCert.checkValidity(new Date());

        interCert.verify(caPublicKey);

        PKCS12BagAttributeCarrier bagAttr = (PKCS12BagAttributeCarrier) interCert;

        //
        // this is actually optional - but if you want to have control
        // over setting the friendly name this is the way to do it...
        //
        bagAttr.setBagAttribute(
                PKCSObjectIdentifiers.pkcs_9_at_friendlyName,
                new DERBMPString("QNE Intermediate Certificate"));


        return interCert;
    }


///////////////////////////////////////////////////////////////////////////////////////////////////
} /////// End Class ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
