package com.ruhuna.event_ticket_management_system.contracts;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/LFDT-web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.7.0.
 */
@SuppressWarnings("rawtypes")
public class TicketManager extends Contract {
    public static final String BINARY = "6080604052348015600f57600080fd5b5033600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610ca1806100606000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c80634c398449146100465780638da5cb5b1461007a578063b598f0be14610098575b600080fd5b610060600480360381019061005b91906104d4565b6100b4565b6040516100719594939291906105fa565b60405180910390f35b610082610192565b60405161008f9190610654565b60405180910390f35b6100b260048036038101906100ad91906107d0565b6101b8565b005b60006020528060005260406000206000915090508060000154908060010180546100dd90610882565b80601f016020809104026020016040519081016040528092919081815260200182805461010990610882565b80156101565780601f1061012b57610100808354040283529160200191610156565b820191906000526020600020905b81548152906001019060200180831161013957829003601f168201915b5050505050908060020154908060030154908060040160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905085565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60008060008681526020019081526020016000206003015414610210576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610207906108ff565b60405180910390fd5b6040518060a001604052808481526020018381526020018281526020014281526020013373ffffffffffffffffffffffffffffffffffffffff1681525060008086815260200190815260200160002060008201518160000155602082015181600101908161027e9190610acb565b50604082015181600201556060820151816003015560808201518160040160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555090505082847f120d541daafcf78bd01efcde569fd0cf3ccaa9b93575552c9d96da5142041609848433426040516103149493929190610b9d565b60405180910390a36103926040518060400160405280601a81526020017f5469636b6574207075626c697368656420776974682049443a20000000000000815250856040518060400160405280600e81526020017f666f72204576656e742049443a2000000000000000000000000000000000000081525086610398565b50505050565b610434848484846040516024016103b29493929190610be9565b6040516020818303038152906040527fc67ea9d1000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff838183161783525050505061043a565b50505050565b61045181610449610454610475565b63ffffffff16565b50565b60006a636f6e736f6c652e6c6f679050600080835160208501845afa505050565b610480819050919050565b610488610c3c565b565b6000604051905090565b600080fd5b600080fd5b6000819050919050565b6104b18161049e565b81146104bc57600080fd5b50565b6000813590506104ce816104a8565b92915050565b6000602082840312156104ea576104e9610494565b5b60006104f8848285016104bf565b91505092915050565b61050a8161049e565b82525050565b600081519050919050565b600082825260208201905092915050565b60005b8381101561054a57808201518184015260208101905061052f565b60008484015250505050565b6000601f19601f8301169050919050565b600061057282610510565b61057c818561051b565b935061058c81856020860161052c565b61059581610556565b840191505092915050565b6000819050919050565b6105b3816105a0565b82525050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60006105e4826105b9565b9050919050565b6105f4816105d9565b82525050565b600060a08201905061060f6000830188610501565b81810360208301526106218187610567565b905061063060408301866105aa565b61063d6060830185610501565b61064a60808301846105eb565b9695505050505050565b600060208201905061066960008301846105eb565b92915050565b600080fd5b600080fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b6106b182610556565b810181811067ffffffffffffffff821117156106d0576106cf610679565b5b80604052505050565b60006106e361048a565b90506106ef82826106a8565b919050565b600067ffffffffffffffff82111561070f5761070e610679565b5b61071882610556565b9050602081019050919050565b82818337600083830152505050565b6000610747610742846106f4565b6106d9565b90508281526020810184848401111561076357610762610674565b5b61076e848285610725565b509392505050565b600082601f83011261078b5761078a61066f565b5b813561079b848260208601610734565b91505092915050565b6107ad816105a0565b81146107b857600080fd5b50565b6000813590506107ca816107a4565b92915050565b600080600080608085870312156107ea576107e9610494565b5b60006107f8878288016104bf565b9450506020610809878288016104bf565b935050604085013567ffffffffffffffff81111561082a57610829610499565b5b61083687828801610776565b9250506060610847878288016107bb565b91505092959194509250565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b6000600282049050600182168061089a57607f821691505b6020821081036108ad576108ac610853565b5b50919050565b7f5469636b657420616c7265616479207075626c69736865642e00000000000000600082015250565b60006108e960198361051b565b91506108f4826108b3565b602082019050919050565b60006020820190508181036000830152610918816108dc565b9050919050565b60008190508160005260206000209050919050565b60006020601f8301049050919050565b600082821b905092915050565b6000600883026109817fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82610944565b61098b8683610944565b95508019841693508086168417925050509392505050565b6000819050919050565b60006109c86109c36109be8461049e565b6109a3565b61049e565b9050919050565b6000819050919050565b6109e2836109ad565b6109f66109ee826109cf565b848454610951565b825550505050565b600090565b610a0b6109fe565b610a168184846109d9565b505050565b5b81811015610a3a57610a2f600082610a03565b600181019050610a1c565b5050565b601f821115610a7f57610a508161091f565b610a5984610934565b81016020851015610a68578190505b610a7c610a7485610934565b830182610a1b565b50505b505050565b600082821c905092915050565b6000610aa260001984600802610a84565b1980831691505092915050565b6000610abb8383610a91565b9150826002028217905092915050565b610ad482610510565b67ffffffffffffffff811115610aed57610aec610679565b5b610af78254610882565b610b02828285610a3e565b600060209050601f831160018114610b355760008415610b23578287015190505b610b2d8582610aaf565b865550610b95565b601f198416610b438661091f565b60005b82811015610b6b57848901518255600182019150602085019450602081019050610b46565b86831015610b885784890151610b84601f891682610a91565b8355505b6001600288020188555050505b505050505050565b60006080820190508181036000830152610bb78187610567565b9050610bc660208301866105aa565b610bd360408301856105eb565b610be06060830184610501565b95945050505050565b60006080820190508181036000830152610c038187610567565b9050610c126020830186610501565b8181036040830152610c248185610567565b9050610c336060830184610501565b95945050505050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052605160045260246000fdfea26469706673582212200d18f110564626328b6c5520fbb15032e6fbd031f0649ed3e5cf86ece5ce16e364736f6c634300081c0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_PUBLISHTICKET = "publishTicket";

    public static final String FUNC_PUBLISHEDTICKETS = "publishedTickets";

    public static final Event TICKETPUBLISHED_EVENT = new Event("TicketPublished", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected TicketManager(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected TicketManager(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected TicketManager(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected TicketManager(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<TicketPublishedEventResponse> getTicketPublishedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(TICKETPUBLISHED_EVENT, transactionReceipt);
        ArrayList<TicketPublishedEventResponse> responses = new ArrayList<TicketPublishedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TicketPublishedEventResponse typedResponse = new TicketPublishedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.ticketId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.eventId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.seat = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.commitmentHash = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.publisher = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static TicketPublishedEventResponse getTicketPublishedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(TICKETPUBLISHED_EVENT, log);
        TicketPublishedEventResponse typedResponse = new TicketPublishedEventResponse();
        typedResponse.log = log;
        typedResponse.ticketId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.eventId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.seat = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.commitmentHash = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.publisher = (String) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
        return typedResponse;
    }

    public Flowable<TicketPublishedEventResponse> ticketPublishedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getTicketPublishedEventFromLog(log));
    }

    public Flowable<TicketPublishedEventResponse> ticketPublishedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TICKETPUBLISHED_EVENT));
        return ticketPublishedEventFlowable(filter);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> publishTicket(BigInteger ticketId,
            BigInteger eventId, String seat, byte[] commitmentHash) {
        final Function function = new Function(
                FUNC_PUBLISHTICKET, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(ticketId), 
                new org.web3j.abi.datatypes.generated.Uint256(eventId), 
                new org.web3j.abi.datatypes.Utf8String(seat), 
                new org.web3j.abi.datatypes.generated.Bytes32(commitmentHash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple5<BigInteger, String, byte[], BigInteger, String>> publishedTickets(
            BigInteger param0) {
        final Function function = new Function(FUNC_PUBLISHEDTICKETS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
        return new RemoteFunctionCall<Tuple5<BigInteger, String, byte[], BigInteger, String>>(function,
                new Callable<Tuple5<BigInteger, String, byte[], BigInteger, String>>() {
                    @Override
                    public Tuple5<BigInteger, String, byte[], BigInteger, String> call() throws
                            Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<BigInteger, String, byte[], BigInteger, String>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (byte[]) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (String) results.get(4).getValue());
                    }
                });
    }

    @Deprecated
    public static TicketManager load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new TicketManager(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static TicketManager load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TicketManager(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static TicketManager load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new TicketManager(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static TicketManager load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TicketManager(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TicketManager> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TicketManager.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<TicketManager> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TicketManager.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<TicketManager> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TicketManager.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<TicketManager> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TicketManager.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static void linkLibraries(List<Contract.LinkReference> references) {
        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class TicketPublishedEventResponse extends BaseEventResponse {
        public BigInteger ticketId;

        public BigInteger eventId;

        public String seat;

        public byte[] commitmentHash;

        public String publisher;

        public BigInteger timestamp;
    }
}
