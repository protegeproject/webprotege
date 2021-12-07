solc --evm-version istanbul --abi --bin --optimize --overwrite -o ./../build/ ../src/main/solidity/*
for f in ../build/*.abi; do 
    cp -- "$f" "../src/main/java/ch/unifr/digits/webprotege/attestation/public/attestation/interfaces/$(basename -- "$f" .abi).json"
    epirus generate solidity generate -a "$f" -o ../build/wrapper/ -p ch.unifr.digits.webprotege.attestation.server.contracts
done
find ../build/wrapper -type f | grep -i java$ | xargs -i cp {} ../src/main/java/ch/unifr/digits/webprotege/attestation/server/contracts

# Testing contracts
solc --evm-version istanbul --abi --bin --optimize --overwrite -o ./../build/testing ../src/test/solidity/*
for f in ../build/testing/*.abi; do 
    epirus generate solidity generate -a "$f" -o ../build/wrapper/testing/ -p ch.unifr.digits.contracts
done
find ../build/wrapper/testing -type f | grep -i java$ | xargs -i cp {} ../src/test/java/ch/unifr/digits/contracts