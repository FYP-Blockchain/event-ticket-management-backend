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
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
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
import org.web3j.tuples.generated.Tuple8;
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
public class EventManager extends Contract {
    public static final String BINARY = "6080604052348015600f57600080fd5b5033600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506001600081905550611d89806100686000396000f3fe608060405234801561001057600080fd5b50600436106100935760003560e01c80639f9d903a116100665780639f9d903a14610139578063d248367714610157578063d78741e314610173578063e6ba2690146101a3578063ec38d5a0146101d357610093565b80630b791430146100985780638da5cb5b146100cf578063957a632e146100ed5780639f6271c314610109575b600080fd5b6100b260048036038101906100ad9190610ea2565b610203565b6040516100c6989796959493929190610fca565b60405180910390f35b6100d7610388565b6040516100e49190611056565b60405180910390f35b61010760048036038101906101029190610ea2565b6103ae565b005b610123600480360381019061011e919061109d565b610568565b6040516101309190611188565b60405180910390f35b6101416105ff565b60405161014e91906111aa565b60405180910390f35b610171600480360381019061016c919061122a565b610605565b005b61018d600480360381019061018891906112f9565b6107ed565b60405161019a91906111aa565b60405180910390f35b6101bd60048036038101906101b89190611339565b61081e565b6040516101ca91906111aa565b60405180910390f35b6101ed60048036038101906101e89190610ea2565b610bc1565b6040516101fa9190611514565b60405180910390f35b600160205280600052604060002060009150905080600001549080600101805461022c90611565565b80601f016020809104026020016040519081016040528092919081815260200182805461025890611565565b80156102a55780601f1061027a576101008083540402835291602001916102a5565b820191906000526020600020905b81548152906001019060200180831161028857829003601f168201915b5050505050908060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060030154908060040154908060050154908060060180546102f290611565565b80601f016020809104026020016040519081016040528092919081815260200182805461031e90611565565b801561036b5780601f106103405761010080835404028352916020019161036b565b820191906000526020600020905b81548152906001019060200180831161034e57829003601f168201915b5050505050908060070160009054906101000a900460ff16905088565b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000600160008381526020019081526020016000209050600060016000848152602001908152602001600020600001540361041e576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610415906115e2565b60405180910390fd5b6001600083815260200190815260200160002060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614806104db5750600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b61051a576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016105119061164e565b60405180910390fd5b60008160070160006101000a81548160ff021916908315150217905550817f9bec5b77a290657edefc9defa075c60a874e90f7cc9496853ca9962d58a4ffdd60405160405180910390a25050565b6060600260008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208054806020026020016040519081016040528092919081815260200182805480156105f357602002820191906000526020600020905b8154815260200190600101908083116105df575b50505050509050919050565b60005481565b6000600160008a815260200190815260200160002090506000600160008b81526020019081526020016000206000015403610675576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161066c906115e2565b60405180910390fd5b8060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614806107205750600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b61075f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016107569061164e565b60405180910390fd5b8787826001019182610772929190611854565b50858160030181905550848482600601918261078f929190611854565b50828160040181905550818160050181905550887fa50dbbdbe33d560afa7865f7dfb02e4856fb0d965cba37c505970776b4be139689898989896040516107da959493929190611960565b60405180910390a2505050505050505050565b6002602052816000526040600020818154811061080957600080fd5b90600052602060002001600091509150505481565b6000808888905011610865576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161085c906119f5565b60405180910390fd5b4286116108a7576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161089e90611a61565b60405180910390fd5b600085116108ea576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016108e190611af3565b60405180910390fd5b6000841161092d576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161092490611b85565b60405180910390fd5b600054905060405180610100016040528082815260200189898080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505081526020013373ffffffffffffffffffffffffffffffffffffffff16815260200187815260200186815260200185815260200184848080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505081526020016001151581525060016000838152602001908152602001600020600082015181600001556020820151816001019081610a3f9190611ba5565b5060408201518160020160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550606082015181600301556080820151816004015560a0820151816005015560c0820151816006019081610aba9190611ba5565b5060e08201518160070160006101000a81548160ff021916908315150217905550905050600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819080600181540180825580915050600190039060005260206000200160009091909190915055600080815480929190610b5690611ca6565b91905055503373ffffffffffffffffffffffffffffffffffffffff16817fcef15f0f10c2f2d51dbb99745c47fabc9591ec875a1d26bdc5e0c2f1e131c4f18a8a8a8a8a8a8a604051610bae9796959493929190611cee565b60405180910390a3979650505050505050565b610bc9610e05565b6000600160008481526020019081526020016000206000015403610c22576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610c19906115e2565b60405180910390fd5b600160008381526020019081526020016000206040518061010001604052908160008201548152602001600182018054610c5b90611565565b80601f0160208091040260200160405190810160405280929190818152602001828054610c8790611565565b8015610cd45780601f10610ca957610100808354040283529160200191610cd4565b820191906000526020600020905b815481529060010190602001808311610cb757829003601f168201915b505050505081526020016002820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001600382015481526020016004820154815260200160058201548152602001600682018054610d6190611565565b80601f0160208091040260200160405190810160405280929190818152602001828054610d8d90611565565b8015610dda5780601f10610daf57610100808354040283529160200191610dda565b820191906000526020600020905b815481529060010190602001808311610dbd57829003601f168201915b505050505081526020016007820160009054906101000a900460ff1615151515815250509050919050565b6040518061010001604052806000815260200160608152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600081526020016000815260200160008152602001606081526020016000151581525090565b600080fd5b600080fd5b6000819050919050565b610e7f81610e6c565b8114610e8a57600080fd5b50565b600081359050610e9c81610e76565b92915050565b600060208284031215610eb857610eb7610e62565b5b6000610ec684828501610e8d565b91505092915050565b610ed881610e6c565b82525050565b600081519050919050565b600082825260208201905092915050565b60005b83811015610f18578082015181840152602081019050610efd565b60008484015250505050565b6000601f19601f8301169050919050565b6000610f4082610ede565b610f4a8185610ee9565b9350610f5a818560208601610efa565b610f6381610f24565b840191505092915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000610f9982610f6e565b9050919050565b610fa981610f8e565b82525050565b60008115159050919050565b610fc481610faf565b82525050565b600061010082019050610fe0600083018b610ecf565b8181036020830152610ff2818a610f35565b90506110016040830189610fa0565b61100e6060830188610ecf565b61101b6080830187610ecf565b61102860a0830186610ecf565b81810360c083015261103a8185610f35565b905061104960e0830184610fbb565b9998505050505050505050565b600060208201905061106b6000830184610fa0565b92915050565b61107a81610f8e565b811461108557600080fd5b50565b60008135905061109781611071565b92915050565b6000602082840312156110b3576110b2610e62565b5b60006110c184828501611088565b91505092915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b6110ff81610e6c565b82525050565b600061111183836110f6565b60208301905092915050565b6000602082019050919050565b6000611135826110ca565b61113f81856110d5565b935061114a836110e6565b8060005b8381101561117b5781516111628882611105565b975061116d8361111d565b92505060018101905061114e565b5085935050505092915050565b600060208201905081810360008301526111a2818461112a565b905092915050565b60006020820190506111bf6000830184610ecf565b92915050565b600080fd5b600080fd5b600080fd5b60008083601f8401126111ea576111e96111c5565b5b8235905067ffffffffffffffff811115611207576112066111ca565b5b602083019150836001820283011115611223576112226111cf565b5b9250929050565b60008060008060008060008060c0898b03121561124a57611249610e62565b5b60006112588b828c01610e8d565b985050602089013567ffffffffffffffff81111561127957611278610e67565b5b6112858b828c016111d4565b975097505060406112988b828c01610e8d565b955050606089013567ffffffffffffffff8111156112b9576112b8610e67565b5b6112c58b828c016111d4565b945094505060806112d88b828c01610e8d565b92505060a06112e98b828c01610e8d565b9150509295985092959890939650565b600080604083850312156113105761130f610e62565b5b600061131e85828601611088565b925050602061132f85828601610e8d565b9150509250929050565b600080600080600080600060a0888a03121561135857611357610e62565b5b600088013567ffffffffffffffff81111561137657611375610e67565b5b6113828a828b016111d4565b975097505060206113958a828b01610e8d565b95505060406113a68a828b01610e8d565b94505060606113b78a828b01610e8d565b935050608088013567ffffffffffffffff8111156113d8576113d7610e67565b5b6113e48a828b016111d4565b925092505092959891949750929550565b600082825260208201905092915050565b600061141182610ede565b61141b81856113f5565b935061142b818560208601610efa565b61143481610f24565b840191505092915050565b61144881610f8e565b82525050565b61145781610faf565b82525050565b60006101008301600083015161147660008601826110f6565b506020830151848203602086015261148e8282611406565b91505060408301516114a3604086018261143f565b5060608301516114b660608601826110f6565b5060808301516114c960808601826110f6565b5060a08301516114dc60a08601826110f6565b5060c083015184820360c08601526114f48282611406565b91505060e083015161150960e086018261144e565b508091505092915050565b6000602082019050818103600083015261152e818461145d565b905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b6000600282049050600182168061157d57607f821691505b6020821081036115905761158f611536565b5b50919050565b7f4576656e7420646f6573206e6f74206578697374000000000000000000000000600082015250565b60006115cc601483610ee9565b91506115d782611596565b602082019050919050565b600060208201905081810360008301526115fb816115bf565b9050919050565b7f4e6f7420617574686f72697a6564000000000000000000000000000000000000600082015250565b6000611638600e83610ee9565b915061164382611602565b602082019050919050565b600060208201905081810360008301526116678161162b565b9050919050565b600082905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b60008190508160005260206000209050919050565b60006020601f8301049050919050565b600082821b905092915050565b60006008830261170a7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff826116cd565b61171486836116cd565b95508019841693508086168417925050509392505050565b6000819050919050565b600061175161174c61174784610e6c565b61172c565b610e6c565b9050919050565b6000819050919050565b61176b83611736565b61177f61177782611758565b8484546116da565b825550505050565b600090565b611794611787565b61179f818484611762565b505050565b5b818110156117c3576117b860008261178c565b6001810190506117a5565b5050565b601f821115611808576117d9816116a8565b6117e2846116bd565b810160208510156117f1578190505b6118056117fd856116bd565b8301826117a4565b50505b505050565b600082821c905092915050565b600061182b6000198460080261180d565b1980831691505092915050565b6000611844838361181a565b9150826002028217905092915050565b61185e838361166e565b67ffffffffffffffff81111561187757611876611679565b5b6118818254611565565b61188c8282856117c7565b6000601f8311600181146118bb57600084156118a9578287013590505b6118b38582611838565b86555061191b565b601f1984166118c9866116a8565b60005b828110156118f1578489013582556001820191506020850194506020810190506118cc565b8683101561190e578489013561190a601f89168261181a565b8355505b6001600288020188555050505b50505050505050565b82818337600083830152505050565b600061193f8385610ee9565b935061194c838584611924565b61195583610f24565b840190509392505050565b6000606082019050818103600083015261197b818789611933565b905061198a6020830186610ecf565b818103604083015261199d818486611933565b90509695505050505050565b7f4576656e74206e616d652063616e6e6f7420626520656d707479000000000000600082015250565b60006119df601a83610ee9565b91506119ea826119a9565b602082019050919050565b60006020820190508181036000830152611a0e816119d2565b9050919050565b7f4576656e742064617465206d75737420626520696e2074686520667574757265600082015250565b6000611a4b602083610ee9565b9150611a5682611a15565b602082019050919050565b60006020820190508181036000830152611a7a81611a3e565b9050919050565b7f546f74616c20737570706c79206d75737420626520677265617465722074686160008201527f6e20300000000000000000000000000000000000000000000000000000000000602082015250565b6000611add602383610ee9565b9150611ae882611a81565b604082019050919050565b60006020820190508181036000830152611b0c81611ad0565b9050919050565b7f5469636b6574207072696365206d75737420626520677265617465722074686160008201527f6e20300000000000000000000000000000000000000000000000000000000000602082015250565b6000611b6f602383610ee9565b9150611b7a82611b13565b604082019050919050565b60006020820190508181036000830152611b9e81611b62565b9050919050565b611bae82610ede565b67ffffffffffffffff811115611bc757611bc6611679565b5b611bd18254611565565b611bdc8282856117c7565b600060209050601f831160018114611c0f5760008415611bfd578287015190505b611c078582611838565b865550611c6f565b601f198416611c1d866116a8565b60005b82811015611c4557848901518255600182019150602085019450602081019050611c20565b86831015611c625784890151611c5e601f89168261181a565b8355505b6001600288020188555050505b505050505050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b6000611cb182610e6c565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8203611ce357611ce2611c77565b5b600182019050919050565b600060a0820190508181036000830152611d0981898b611933565b9050611d186020830188610ecf565b611d256040830187610ecf565b611d326060830186610ecf565b8181036080830152611d45818486611933565b90509897505050505050505056fea2646970667358221220d0c5d740d2c3e1c3fbd9046c689bb15fe023c735bd2b09044fae47fbc0c88ddd64736f6c634300081c0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_CREATEEVENT = "createEvent";

    public static final String FUNC_DEACTIVATEEVENT = "deactivateEvent";

    public static final String FUNC_EVENTS = "events";

    public static final String FUNC_EVENTSBYORGANIZER = "eventsByOrganizer";

    public static final String FUNC_GETEVENTDETAILS = "getEventDetails";

    public static final String FUNC_GETEVENTSBYORGANIZER = "getEventsByOrganizer";

    public static final String FUNC_NEXTEVENTID = "nextEventId";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_UPDATEEVENTDETAILS = "updateEventDetails";

    public static final org.web3j.abi.datatypes.Event EVENTCREATED_EVENT = new org.web3j.abi.datatypes.Event("EventCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final org.web3j.abi.datatypes.Event EVENTDEACTIVATED_EVENT = new org.web3j.abi.datatypes.Event("EventDeactivated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}));
    ;

    public static final org.web3j.abi.datatypes.Event EVENTDETAILSUPDATED_EVENT = new org.web3j.abi.datatypes.Event("EventDetailsUpdated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected EventManager(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected EventManager(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected EventManager(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected EventManager(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<EventCreatedEventResponse> getEventCreatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(EVENTCREATED_EVENT, transactionReceipt);
        ArrayList<EventCreatedEventResponse> responses = new ArrayList<EventCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            EventCreatedEventResponse typedResponse = new EventCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.eventId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.organizer = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.eventDate = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.totalSupply = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.ticketPrice = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.metadataURI = (String) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static EventCreatedEventResponse getEventCreatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(EVENTCREATED_EVENT, log);
        EventCreatedEventResponse typedResponse = new EventCreatedEventResponse();
        typedResponse.log = log;
        typedResponse.eventId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.organizer = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.eventDate = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.totalSupply = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.ticketPrice = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
        typedResponse.metadataURI = (String) eventValues.getNonIndexedValues().get(4).getValue();
        return typedResponse;
    }

    public Flowable<EventCreatedEventResponse> eventCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getEventCreatedEventFromLog(log));
    }

    public Flowable<EventCreatedEventResponse> eventCreatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(EVENTCREATED_EVENT));
        return eventCreatedEventFlowable(filter);
    }

    public static List<EventDeactivatedEventResponse> getEventDeactivatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(EVENTDEACTIVATED_EVENT, transactionReceipt);
        ArrayList<EventDeactivatedEventResponse> responses = new ArrayList<EventDeactivatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            EventDeactivatedEventResponse typedResponse = new EventDeactivatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.eventId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static EventDeactivatedEventResponse getEventDeactivatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(EVENTDEACTIVATED_EVENT, log);
        EventDeactivatedEventResponse typedResponse = new EventDeactivatedEventResponse();
        typedResponse.log = log;
        typedResponse.eventId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<EventDeactivatedEventResponse> eventDeactivatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getEventDeactivatedEventFromLog(log));
    }

    public Flowable<EventDeactivatedEventResponse> eventDeactivatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(EVENTDEACTIVATED_EVENT));
        return eventDeactivatedEventFlowable(filter);
    }

    public static List<EventDetailsUpdatedEventResponse> getEventDetailsUpdatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(EVENTDETAILSUPDATED_EVENT, transactionReceipt);
        ArrayList<EventDetailsUpdatedEventResponse> responses = new ArrayList<EventDetailsUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            EventDetailsUpdatedEventResponse typedResponse = new EventDetailsUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.eventId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newName = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newEventDate = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.newMetadataURI = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static EventDetailsUpdatedEventResponse getEventDetailsUpdatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(EVENTDETAILSUPDATED_EVENT, log);
        EventDetailsUpdatedEventResponse typedResponse = new EventDetailsUpdatedEventResponse();
        typedResponse.log = log;
        typedResponse.eventId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.newName = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.newEventDate = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.newMetadataURI = (String) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<EventDetailsUpdatedEventResponse> eventDetailsUpdatedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getEventDetailsUpdatedEventFromLog(log));
    }

    public Flowable<EventDetailsUpdatedEventResponse> eventDetailsUpdatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(EVENTDETAILSUPDATED_EVENT));
        return eventDetailsUpdatedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> createEvent(String _name, BigInteger _eventDate,
            BigInteger _totalSupply, BigInteger _ticketPrice, String _metadataURI) {
        final Function function = new Function(
                FUNC_CREATEEVENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.generated.Uint256(_eventDate), 
                new org.web3j.abi.datatypes.generated.Uint256(_totalSupply), 
                new org.web3j.abi.datatypes.generated.Uint256(_ticketPrice), 
                new org.web3j.abi.datatypes.Utf8String(_metadataURI)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> deactivateEvent(BigInteger _eventId) {
        final Function function = new Function(
                FUNC_DEACTIVATEEVENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple8<BigInteger, String, String, BigInteger, BigInteger, BigInteger, String, Boolean>> events(
            BigInteger param0) {
        final Function function = new Function(FUNC_EVENTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple8<BigInteger, String, String, BigInteger, BigInteger, BigInteger, String, Boolean>>(function,
                new Callable<Tuple8<BigInteger, String, String, BigInteger, BigInteger, BigInteger, String, Boolean>>() {
                    @Override
                    public Tuple8<BigInteger, String, String, BigInteger, BigInteger, BigInteger, String, Boolean> call(
                            ) throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple8<BigInteger, String, String, BigInteger, BigInteger, BigInteger, String, Boolean>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue(), 
                                (String) results.get(6).getValue(), 
                                (Boolean) results.get(7).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> eventsByOrganizer(String param0, BigInteger param1) {
        final Function function = new Function(FUNC_EVENTSBYORGANIZER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0), 
                new org.web3j.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Event> getEventDetails(BigInteger _eventId) {
        final Function function = new Function(FUNC_GETEVENTDETAILS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Event>() {}));
        return executeRemoteCallSingleValueReturn(function, Event.class);
    }

    public RemoteFunctionCall<List> getEventsByOrganizer(String _organizer) {
        final Function function = new Function(FUNC_GETEVENTSBYORGANIZER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _organizer)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> nextEventId() {
        final Function function = new Function(FUNC_NEXTEVENTID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> updateEventDetails(BigInteger _eventId,
            String _name, BigInteger _eventDate, String _metadataURI, BigInteger _newTotalSupply,
            BigInteger _newTicketPrice) {
        final Function function = new Function(
                FUNC_UPDATEEVENTDETAILS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventId), 
                new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.generated.Uint256(_eventDate), 
                new org.web3j.abi.datatypes.Utf8String(_metadataURI), 
                new org.web3j.abi.datatypes.generated.Uint256(_newTotalSupply), 
                new org.web3j.abi.datatypes.generated.Uint256(_newTicketPrice)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static EventManager load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new EventManager(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static EventManager load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new EventManager(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static EventManager load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new EventManager(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static EventManager load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new EventManager(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<EventManager> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(EventManager.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<EventManager> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(EventManager.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<EventManager> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(EventManager.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<EventManager> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(EventManager.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
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

    public static class Event extends DynamicStruct {
        public BigInteger id;

        public String name;

        public String organizer;

        public BigInteger eventDate;

        public BigInteger totalSupply;

        public BigInteger ticketPrice;

        public String metadataURI;

        public Boolean isActive;

        public Event(BigInteger id, String name, String organizer, BigInteger eventDate,
                BigInteger totalSupply, BigInteger ticketPrice, String metadataURI,
                Boolean isActive) {
            super(new org.web3j.abi.datatypes.generated.Uint256(id), 
                    new org.web3j.abi.datatypes.Utf8String(name), 
                    new org.web3j.abi.datatypes.Address(160, organizer), 
                    new org.web3j.abi.datatypes.generated.Uint256(eventDate), 
                    new org.web3j.abi.datatypes.generated.Uint256(totalSupply), 
                    new org.web3j.abi.datatypes.generated.Uint256(ticketPrice), 
                    new org.web3j.abi.datatypes.Utf8String(metadataURI), 
                    new org.web3j.abi.datatypes.Bool(isActive));
            this.id = id;
            this.name = name;
            this.organizer = organizer;
            this.eventDate = eventDate;
            this.totalSupply = totalSupply;
            this.ticketPrice = ticketPrice;
            this.metadataURI = metadataURI;
            this.isActive = isActive;
        }

        public Event(Uint256 id, Utf8String name, Address organizer, Uint256 eventDate,
                Uint256 totalSupply, Uint256 ticketPrice, Utf8String metadataURI, Bool isActive) {
            super(id, name, organizer, eventDate, totalSupply, ticketPrice, metadataURI, isActive);
            this.id = id.getValue();
            this.name = name.getValue();
            this.organizer = organizer.getValue();
            this.eventDate = eventDate.getValue();
            this.totalSupply = totalSupply.getValue();
            this.ticketPrice = ticketPrice.getValue();
            this.metadataURI = metadataURI.getValue();
            this.isActive = isActive.getValue();
        }
    }

    public static class EventCreatedEventResponse extends BaseEventResponse {
        public BigInteger eventId;

        public String organizer;

        public String name;

        public BigInteger eventDate;

        public BigInteger totalSupply;

        public BigInteger ticketPrice;

        public String metadataURI;
    }

    public static class EventDeactivatedEventResponse extends BaseEventResponse {
        public BigInteger eventId;
    }

    public static class EventDetailsUpdatedEventResponse extends BaseEventResponse {
        public BigInteger eventId;

        public String newName;

        public BigInteger newEventDate;

        public String newMetadataURI;
    }
}
