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
    public static final String BINARY = "6080604052348015600f57600080fd5b5033600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506001600081905550611d5f806100686000396000f3fe608060405234801561001057600080fd5b50600436106100935760003560e01c80639f6271c3116100665780639f6271c3146101255780639f9d903a14610155578063d78741e314610173578063e6ba2690146101a3578063ec38d5a0146101d357610093565b80630b791430146100985780638da5cb5b146100cf57806393fd53c9146100ed578063957a632e14610109575b600080fd5b6100b260048036038101906100ad9190610ea0565b610203565b6040516100c6989796959493929190610fc8565b60405180910390f35b6100d7610388565b6040516100e49190611054565b60405180910390f35b610107600480360381019061010291906110d4565b6103ae565b005b610123600480360381019061011e9190610ea0565b610594565b005b61013f600480360381019061013a91906111a7565b61074e565b60405161014c9190611292565b60405180910390f35b61015d6107e5565b60405161016a91906112b4565b60405180910390f35b61018d600480360381019061018891906112cf565b6107eb565b60405161019a91906112b4565b60405180910390f35b6101bd60048036038101906101b8919061130f565b61081c565b6040516101ca91906112b4565b60405180910390f35b6101ed60048036038101906101e89190610ea0565b610bbf565b6040516101fa91906114ea565b60405180910390f35b600160205280600052604060002060009150905080600001549080600101805461022c9061153b565b80601f01602080910402602001604051908101604052809291908181526020018280546102589061153b565b80156102a55780601f1061027a576101008083540402835291602001916102a5565b820191906000526020600020905b81548152906001019060200180831161028857829003601f168201915b5050505050908060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060030154908060040154908060050154908060060180546102f29061153b565b80601f016020809104026020016040519081016040528092919081815260200182805461031e9061153b565b801561036b5780601f106103405761010080835404028352916020019161036b565b820191906000526020600020905b81548152906001019060200180831161034e57829003601f168201915b5050505050908060070160009054906101000a900460ff16905088565b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000600160008881526020019081526020016000209050600060016000898152602001908152602001600020600001540361041e576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610415906115b8565b60405180910390fd5b6001600088815260200190815260200160002060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614806104db5750600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b61051a576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161051190611624565b60405180910390fd5b858582600101918261052d92919061182a565b50838160030181905550828282600601918261054a92919061182a565b50867fa50dbbdbe33d560afa7865f7dfb02e4856fb0d965cba37c505970776b4be13968787878787604051610583959493929190611936565b60405180910390a250505050505050565b60006001600083815260200190815260200160002090506000600160008481526020019081526020016000206000015403610604576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016105fb906115b8565b60405180910390fd5b6001600083815260200190815260200160002060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614806106c15750600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b610700576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016106f790611624565b60405180910390fd5b60008160070160006101000a81548160ff021916908315150217905550817f9bec5b77a290657edefc9defa075c60a874e90f7cc9496853ca9962d58a4ffdd60405160405180910390a25050565b6060600260008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208054806020026020016040519081016040528092919081815260200182805480156107d957602002820191906000526020600020905b8154815260200190600101908083116107c5575b50505050509050919050565b60005481565b6002602052816000526040600020818154811061080757600080fd5b90600052602060002001600091509150505481565b6000808888905011610863576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161085a906119cb565b60405180910390fd5b4286116108a5576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161089c90611a37565b60405180910390fd5b600085116108e8576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016108df90611ac9565b60405180910390fd5b6000841161092b576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161092290611b5b565b60405180910390fd5b600054905060405180610100016040528082815260200189898080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505081526020013373ffffffffffffffffffffffffffffffffffffffff16815260200187815260200186815260200185815260200184848080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505081526020016001151581525060016000838152602001908152602001600020600082015181600001556020820151816001019081610a3d9190611b7b565b5060408201518160020160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550606082015181600301556080820151816004015560a0820151816005015560c0820151816006019081610ab89190611b7b565b5060e08201518160070160006101000a81548160ff021916908315150217905550905050600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819080600181540180825580915050600190039060005260206000200160009091909190915055600080815480929190610b5490611c7c565b91905055503373ffffffffffffffffffffffffffffffffffffffff16817fcef15f0f10c2f2d51dbb99745c47fabc9591ec875a1d26bdc5e0c2f1e131c4f18a8a8a8a8a8a8a604051610bac9796959493929190611cc4565b60405180910390a3979650505050505050565b610bc7610e03565b6000600160008481526020019081526020016000206000015403610c20576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610c17906115b8565b60405180910390fd5b600160008381526020019081526020016000206040518061010001604052908160008201548152602001600182018054610c599061153b565b80601f0160208091040260200160405190810160405280929190818152602001828054610c859061153b565b8015610cd25780601f10610ca757610100808354040283529160200191610cd2565b820191906000526020600020905b815481529060010190602001808311610cb557829003601f168201915b505050505081526020016002820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001600382015481526020016004820154815260200160058201548152602001600682018054610d5f9061153b565b80601f0160208091040260200160405190810160405280929190818152602001828054610d8b9061153b565b8015610dd85780601f10610dad57610100808354040283529160200191610dd8565b820191906000526020600020905b815481529060010190602001808311610dbb57829003601f168201915b505050505081526020016007820160009054906101000a900460ff1615151515815250509050919050565b6040518061010001604052806000815260200160608152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600081526020016000815260200160008152602001606081526020016000151581525090565b600080fd5b600080fd5b6000819050919050565b610e7d81610e6a565b8114610e8857600080fd5b50565b600081359050610e9a81610e74565b92915050565b600060208284031215610eb657610eb5610e60565b5b6000610ec484828501610e8b565b91505092915050565b610ed681610e6a565b82525050565b600081519050919050565b600082825260208201905092915050565b60005b83811015610f16578082015181840152602081019050610efb565b60008484015250505050565b6000601f19601f8301169050919050565b6000610f3e82610edc565b610f488185610ee7565b9350610f58818560208601610ef8565b610f6181610f22565b840191505092915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000610f9782610f6c565b9050919050565b610fa781610f8c565b82525050565b60008115159050919050565b610fc281610fad565b82525050565b600061010082019050610fde600083018b610ecd565b8181036020830152610ff0818a610f33565b9050610fff6040830189610f9e565b61100c6060830188610ecd565b6110196080830187610ecd565b61102660a0830186610ecd565b81810360c08301526110388185610f33565b905061104760e0830184610fb9565b9998505050505050505050565b60006020820190506110696000830184610f9e565b92915050565b600080fd5b600080fd5b600080fd5b60008083601f8401126110945761109361106f565b5b8235905067ffffffffffffffff8111156110b1576110b0611074565b5b6020830191508360018202830111156110cd576110cc611079565b5b9250929050565b600080600080600080608087890312156110f1576110f0610e60565b5b60006110ff89828a01610e8b565b965050602087013567ffffffffffffffff8111156111205761111f610e65565b5b61112c89828a0161107e565b9550955050604061113f89828a01610e8b565b935050606087013567ffffffffffffffff8111156111605761115f610e65565b5b61116c89828a0161107e565b92509250509295509295509295565b61118481610f8c565b811461118f57600080fd5b50565b6000813590506111a18161117b565b92915050565b6000602082840312156111bd576111bc610e60565b5b60006111cb84828501611192565b91505092915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b61120981610e6a565b82525050565b600061121b8383611200565b60208301905092915050565b6000602082019050919050565b600061123f826111d4565b61124981856111df565b9350611254836111f0565b8060005b8381101561128557815161126c888261120f565b975061127783611227565b925050600181019050611258565b5085935050505092915050565b600060208201905081810360008301526112ac8184611234565b905092915050565b60006020820190506112c96000830184610ecd565b92915050565b600080604083850312156112e6576112e5610e60565b5b60006112f485828601611192565b925050602061130585828601610e8b565b9150509250929050565b600080600080600080600060a0888a03121561132e5761132d610e60565b5b600088013567ffffffffffffffff81111561134c5761134b610e65565b5b6113588a828b0161107e565b9750975050602061136b8a828b01610e8b565b955050604061137c8a828b01610e8b565b945050606061138d8a828b01610e8b565b935050608088013567ffffffffffffffff8111156113ae576113ad610e65565b5b6113ba8a828b0161107e565b925092505092959891949750929550565b600082825260208201905092915050565b60006113e782610edc565b6113f181856113cb565b9350611401818560208601610ef8565b61140a81610f22565b840191505092915050565b61141e81610f8c565b82525050565b61142d81610fad565b82525050565b60006101008301600083015161144c6000860182611200565b506020830151848203602086015261146482826113dc565b91505060408301516114796040860182611415565b50606083015161148c6060860182611200565b50608083015161149f6080860182611200565b5060a08301516114b260a0860182611200565b5060c083015184820360c08601526114ca82826113dc565b91505060e08301516114df60e0860182611424565b508091505092915050565b600060208201905081810360008301526115048184611433565b905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b6000600282049050600182168061155357607f821691505b6020821081036115665761156561150c565b5b50919050565b7f4576656e7420646f6573206e6f74206578697374000000000000000000000000600082015250565b60006115a2601483610ee7565b91506115ad8261156c565b602082019050919050565b600060208201905081810360008301526115d181611595565b9050919050565b7f4e6f7420617574686f72697a6564000000000000000000000000000000000000600082015250565b600061160e600e83610ee7565b9150611619826115d8565b602082019050919050565b6000602082019050818103600083015261163d81611601565b9050919050565b600082905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b60008190508160005260206000209050919050565b60006020601f8301049050919050565b600082821b905092915050565b6000600883026116e07fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff826116a3565b6116ea86836116a3565b95508019841693508086168417925050509392505050565b6000819050919050565b600061172761172261171d84610e6a565b611702565b610e6a565b9050919050565b6000819050919050565b6117418361170c565b61175561174d8261172e565b8484546116b0565b825550505050565b600090565b61176a61175d565b611775818484611738565b505050565b5b818110156117995761178e600082611762565b60018101905061177b565b5050565b601f8211156117de576117af8161167e565b6117b884611693565b810160208510156117c7578190505b6117db6117d385611693565b83018261177a565b50505b505050565b600082821c905092915050565b6000611801600019846008026117e3565b1980831691505092915050565b600061181a83836117f0565b9150826002028217905092915050565b6118348383611644565b67ffffffffffffffff81111561184d5761184c61164f565b5b611857825461153b565b61186282828561179d565b6000601f831160018114611891576000841561187f578287013590505b611889858261180e565b8655506118f1565b601f19841661189f8661167e565b60005b828110156118c7578489013582556001820191506020850194506020810190506118a2565b868310156118e457848901356118e0601f8916826117f0565b8355505b6001600288020188555050505b50505050505050565b82818337600083830152505050565b60006119158385610ee7565b93506119228385846118fa565b61192b83610f22565b840190509392505050565b60006060820190508181036000830152611951818789611909565b90506119606020830186610ecd565b8181036040830152611973818486611909565b90509695505050505050565b7f4576656e74206e616d652063616e6e6f7420626520656d707479000000000000600082015250565b60006119b5601a83610ee7565b91506119c08261197f565b602082019050919050565b600060208201905081810360008301526119e4816119a8565b9050919050565b7f4576656e742064617465206d75737420626520696e2074686520667574757265600082015250565b6000611a21602083610ee7565b9150611a2c826119eb565b602082019050919050565b60006020820190508181036000830152611a5081611a14565b9050919050565b7f546f74616c20737570706c79206d75737420626520677265617465722074686160008201527f6e20300000000000000000000000000000000000000000000000000000000000602082015250565b6000611ab3602383610ee7565b9150611abe82611a57565b604082019050919050565b60006020820190508181036000830152611ae281611aa6565b9050919050565b7f5469636b6574207072696365206d75737420626520677265617465722074686160008201527f6e20300000000000000000000000000000000000000000000000000000000000602082015250565b6000611b45602383610ee7565b9150611b5082611ae9565b604082019050919050565b60006020820190508181036000830152611b7481611b38565b9050919050565b611b8482610edc565b67ffffffffffffffff811115611b9d57611b9c61164f565b5b611ba7825461153b565b611bb282828561179d565b600060209050601f831160018114611be55760008415611bd3578287015190505b611bdd858261180e565b865550611c45565b601f198416611bf38661167e565b60005b82811015611c1b57848901518255600182019150602085019450602081019050611bf6565b86831015611c385784890151611c34601f8916826117f0565b8355505b6001600288020188555050505b505050505050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b6000611c8782610e6a565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8203611cb957611cb8611c4d565b5b600182019050919050565b600060a0820190508181036000830152611cdf81898b611909565b9050611cee6020830188610ecd565b611cfb6040830187610ecd565b611d086060830186610ecd565b8181036080830152611d1b818486611909565b90509897505050505050505056fea2646970667358221220b95183cc02d11b2944afb00061a5c1d719fa868bb69d10d68db0f234594d894f64736f6c634300081c0033";

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
            String _name, BigInteger _eventDate, String _metadataURI) {
        final Function function = new Function(
                FUNC_UPDATEEVENTDETAILS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_eventId), 
                new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.generated.Uint256(_eventDate), 
                new org.web3j.abi.datatypes.Utf8String(_metadataURI)), 
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
