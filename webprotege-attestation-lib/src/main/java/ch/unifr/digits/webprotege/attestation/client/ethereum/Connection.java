package ch.unifr.digits.webprotege.attestation.client.ethereum;


import ch.unifr.digits.webprotege.attestation.client.web3.Web3;

public class Connection {
    private EthereumProvider provider;
    private Web3 web3;

    public Connection(EthereumProvider provider, Web3 web3) {
        this.provider = provider;
        this.web3 = web3;
    }

    public EthereumProvider getProvider() {
        return provider;
    }

    public Web3 getWeb3() {
        return web3;
    }
}
