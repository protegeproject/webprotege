package ch.unifr.digits.webprotege.attestation.client;


import ch.unifr.digits.webprotege.attestation.client.contract.ChangeTrackingContract;
import ch.unifr.digits.webprotege.attestation.client.contract.OntologyAttestationContract;
import ch.unifr.digits.webprotege.attestation.client.contract.VerifyContractReturn;
import ch.unifr.digits.webprotege.attestation.client.ethereum.Connection;
import ch.unifr.digits.webprotege.attestation.client.ethereum.EthereumProvider;
import ch.unifr.digits.webprotege.attestation.client.web3.Web3;
import ch.unifr.digits.webprotege.attestation.client.web3.core.TransactionReceipt;
import ch.unifr.digits.webprotege.attestation.shared.VerifyResult;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.*;
import edu.stanford.bmir.protege.web.shared.download.DownloadFormatExtension;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import elemental2.promise.Promise;
import org.semanticweb.owlapi.model.OWLEntity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class ClientAttestationService {

    private static final String CONTRACT_ONTOLOGY_NAME = "OntologyAttestation";

    public static void signProjectFile(ProjectId projectId, RevisionNumber revisionNumber, String ontologyIRI,
                                       String versionIRI, String name, String address, Callback<Boolean, Object> callback) {
        DownloadFormatExtension extension = DownloadFormatExtension.owl;
        ProjectDeflateDownloader downloader = new ProjectDeflateDownloader(projectId, revisionNumber, extension);

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        Promise<String> hashPromise = downloader.download().then(ontology -> {
            byte[] bytes = digest.digest(ontology.getBytes());
            String hash = bytesToHex(bytes);
            GWT.log("[attestation] Ontology hash: " + hash);
            return Promise.resolve(hash);
        });

        hashPromise.then((hash) -> {
            signOntology(ontologyIRI, versionIRI, name, address, hash, callback);
            return null;
        });
    }

    public static void verifyProjectFile(ProjectId projectId, RevisionNumber revisionNumber, String ontologyIRI,
                                         String versionIRI, String address, Callback<VerifyResult, Object> callback) {
        DownloadFormatExtension extension = DownloadFormatExtension.owl;
        ProjectDeflateDownloader downloader = new ProjectDeflateDownloader(projectId, revisionNumber, extension);

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        Promise<String> hashPromise = downloader.download().then(ontology -> {
            byte[] bytes = digest.digest(ontology.getBytes());
            String hash = bytesToHex(bytes);
            GWT.log("[attestation] Ontology hash: " + hash);
            return Promise.resolve(hash);
        });

        hashPromise.then((hash) -> {
            verifyOntology(ontologyIRI, versionIRI, address, hash, callback);
            return null;
        });
    }

    public static void signOntology(String ontologyIRI, String versionIRI, String name, String address, String hash,
                                    Callback<Boolean, Object> callback) {
        Promise<Connection> connectionPromise = connectToChain();
        Promise<Object> interfacePromise = getContractInterface(CONTRACT_ONTOLOGY_NAME);
        Promise.all(connectionPromise, interfacePromise).then((args) -> {
            Connection connection = (Connection) args[0];
            Object contractInterface = args[1];

            OntologyAttestationContract contract = new OntologyAttestationContract(connection.getWeb3(), contractInterface, address);
            Promise<TransactionReceipt> attestPromise = contract.attest(connection.getProvider().selectedAddress,
                    ontologyIRI, versionIRI, name, hash);
            attestPromise.then(receipt -> {
                GWT.log("[attestation] transaction result: " + receipt.status);
                callback.onSuccess(receipt.status);
                return null;
            }).catch_(error -> {
                callback.onFailure(error);
                return null;
            });

            return null;
        });
    }

    public static void verifyOntology(String ontologyIRI, String versionIRI, String address, String hash,
                                      Callback<VerifyResult, Object> callback) {

        Promise<Connection> connectionPromise = connectToChain();
        Promise<Object> interfacePromise = getContractInterface(CONTRACT_ONTOLOGY_NAME);

        Promise.all(connectionPromise, interfacePromise).then((args) -> {
            Connection connection = (Connection) args[0];
            Object contractInterface = args[1];

            OntologyAttestationContract contract = new OntologyAttestationContract(connection.getWeb3(), contractInterface, address);
            Promise<VerifyContractReturn> resultPromise = contract.verify(connection.getProvider().selectedAddress,
                    ontologyIRI, versionIRI, hash);
            resultPromise.then(contractReturn -> {
                VerifyResult result = new VerifyResult(contractReturn.valid, contractReturn.signer,
                        contractReturn.signerName, contractReturn.timestamp);
                GWT.log("[attestation] verify result: " + result.toString());
                callback.onSuccess(result);
                return null;
            }).catch_(error -> {
                callback.onFailure(error);
                return null;
            });
            return null;
        });

    }

    public static void signChangeTracking(String ontologyIRI, String versionIRI, String name, String address, String hash,
                                    List<Integer> classHashes, Callback<Boolean, Object> callback) {
        Promise<Connection> connectionPromise = connectToChain();
        Promise<Object> interfacePromise = getContractInterface(CONTRACT_ONTOLOGY_NAME);
        Promise.all(connectionPromise, interfacePromise).then((args) -> {
            Connection connection = (Connection) args[0];
            Object contractInterface = args[1];

            ChangeTrackingContract contract = new ChangeTrackingContract(connection.getWeb3(), contractInterface, address);
            Promise<TransactionReceipt> attestPromise = contract.attest(connection.getProvider().selectedAddress,
                    ontologyIRI, versionIRI, name, hash, classHashes);
            attestPromise.then(receipt -> {
                GWT.log("[attestation] transaction result: " + receipt.status);
                callback.onSuccess(receipt.status);
                return null;
            }).catch_(error -> {
                callback.onFailure(error);
                return null;
            });

            return null;
        });
    }

    public static void verifyChangeTracking(String ontologyIRI, String versionIRI, String address, String hash,
                                      Callback<VerifyResult, Object> callback) {

        Promise<Connection> connectionPromise = connectToChain();
        Promise<Object> interfacePromise = getContractInterface(CONTRACT_ONTOLOGY_NAME);

        Promise.all(connectionPromise, interfacePromise).then((args) -> {
            Connection connection = (Connection) args[0];
            Object contractInterface = args[1];

            ChangeTrackingContract contract = new ChangeTrackingContract(connection.getWeb3(), contractInterface, address);
            Promise<VerifyContractReturn> resultPromise = contract.verify(connection.getProvider().selectedAddress,
                    ontologyIRI, versionIRI, hash);
            resultPromise.then(contractReturn -> {
                VerifyResult result = new VerifyResult(contractReturn.valid, contractReturn.signer,
                        contractReturn.signerName, contractReturn.timestamp);
                GWT.log("[attestation] verify result: " + result.toString());
                callback.onSuccess(result);
                return null;
            }).catch_(error -> {
                callback.onFailure(error);
                return null;
            });
            return null;
        });

    }

    public static String hashData(byte[] data) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data);
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static int hashEntity(OWLEntity entity) {
        return entity.hashCode();
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static Promise<Connection> connectToChain() {
        Promise<Connection> promise = new Promise<>((resolve, reject) -> {
            EthereumProvider.detectEthereumProvider().then(p -> {
                Web3 web3 = new Web3(p);

                web3.eth.requestAccounts().then(accounts -> {
                    GWT.log("[attestation] connected to chain.");
                    Connection connection = new Connection(p, web3);
                    resolve.onInvoke(connection);
                    return null;
                }).catch_(error -> {
                    reject.onInvoke(error);
                    return null;
                });
                return null;
            }).catch_(error -> {
                reject.onInvoke(error);
                return null;
            });
        });
        return promise;
    }

    private static Promise<Object> getContractInterface(String contractName) {
        String url = GWT.getModuleBaseForStaticFiles() + "attestation/interfaces/"+contractName+".json";
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        requestBuilder.setRequestData(null);

        Promise<Object> promise = new Promise<>((resolve, reject) -> {
            requestBuilder.setCallback(new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    String json = response.getText();
                    Object object = JsonUtils.unsafeEval(json);
                    GWT.log("[attestation] retrieved contract interface.");
                    resolve.onInvoke(object);
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    reject.onInvoke(exception);
                }
            });
            try {
                requestBuilder.send();
            } catch (RequestException e) {
                reject.onInvoke(e);
            }
        });

        return promise;
    }


}
