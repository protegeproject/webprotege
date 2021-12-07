package ch.unifr.digits.webprotege.attestation.client.web3.eth;


import ch.unifr.digits.webprotege.attestation.client.web3.eth.contract.Contract;
import com.google.gwt.core.client.JsArrayString;
import elemental2.promise.Promise;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class Eth {

    @JsMethod(name = "Contract")
    public native Contract newContract(Object jsonInterface, String address);

    @JsMethod
    public native Promise<JsArrayString> getAccounts();

    @JsMethod
    public native Promise<JsArrayString> requestAccounts();

    @JsMethod
    public native Promise<String> getBalance(String address);

}
