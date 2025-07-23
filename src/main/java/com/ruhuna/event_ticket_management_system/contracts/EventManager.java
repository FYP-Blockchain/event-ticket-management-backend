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
    public static final String BINARY = "6080604052348015600f57600080fd5b5033600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506001600081905550612110806100686000396000f3fe608060405234801561001057600080fd5b50600436106100935760003560e01c80639f6271c3116100665780639f6271c3146101255780639f9d903a14610155578063d78741e314610173578063e6ba2690146101a3578063ec38d5a0146101d357610093565b80630b791430146100985780638da5cb5b146100cf57806393fd53c9146100ed578063957a632e14610109575b600080fd5b6100b260048036038101906100ad91906111dd565b610203565b6040516100c6989796959493929190611305565b60405180910390f35b6100d7610388565b6040516100e49190611391565b60405180910390f35b61010760048036038101906101029190611411565b6103ae565b005b610123600480360381019061011e91906111dd565b610572565b005b61013f600480360381019061013a91906114e4565b6106f9565b60405161014c91906115cf565b60405180910390f35b61015d610790565b60405161016a91906115f1565b60405180910390f35b61018d6004803603810190610188919061160c565b610796565b60405161019a91906115f1565b60405180910390f35b6101bd60048036038101906101b8919061164c565b6107c7565b6040516101ca91906115f1565b60405180910390f35b6101ed60048036038101906101e891906111dd565b610cd5565b6040516101fa9190611827565b60405180910390f35b600160205280600052604060002060009150905080600001549080600101805461022c90611878565b80601f016020809104026020016040519081016040528092919081815260200182805461025890611878565b80156102a55780601f1061027a576101008083540402835291602001916102a5565b820191906000526020600020905b81548152906001019060200180831161028857829003601f168201915b5050505050908060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060030154908060040154908060050154908060060180546102f290611878565b80601f016020809104026020016040519081016040528092919081815260200182805461031e90611878565b801561036b5780601f106103405761010080835404028352916020019161036b565b820191906000526020600020905b81548152906001019060200180831161034e57829003601f168201915b5050505050908060070160009054906101000a900460ff16905088565b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000600160008881526020019081526020016000206000015403610407576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016103fe906118f5565b60405180910390fd5b6001600087815260200190815260200160002060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614806104c45750600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b610503576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016104fa90611961565b60405180910390fd5b8484600160008981526020019081526020016000206001019182610528929190611b67565b508260016000888152602001908152602001600020600301819055508181600160008981526020019081526020016000206006019182610569929190611b67565b50505050505050565b60006001600083815260200190815260200160002060000154036105cb576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016105c2906118f5565b60405180910390fd5b6001600082815260200190815260200160002060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614806106885750600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b6106c7576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016106be90611961565b60405180910390fd5b60006001600083815260200190815260200160002060070160006101000a81548160ff02191690831515021790555050565b6060600260008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002080548060200260200160405190810160405280929190818152602001828054801561078457602002820191906000526020600020905b815481526020019060010190808311610770575b50505050509050919050565b60005481565b600260205281600052604060002081815481106107b257600080fd5b90600052602060002001600091509150505481565b600080888890501161080e576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161080590611c83565b60405180910390fd5b428611610850576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161084790611cef565b60405180910390fd5b60008511610893576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161088a90611d81565b60405180910390fd5b600084116108d6576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016108cd90611e13565b60405180910390fd5b6109146040518060400160405280601681526020017f312e20526571756972656d656e74732070617373656400000000000000000000815250610f19565b60005490506109586040518060400160405280600f81526020017f322e2041737369676e65642049443a000000000000000000000000000000000081525082610fb2565b60405180610100016040528082815260200189898080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505081526020013373ffffffffffffffffffffffffffffffffffffffff16815260200187815260200186815260200185815260200184848080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505081526020016001151581525060016000838152602001908152602001600020600082015181600001556020820151816001019081610a659190611e33565b5060408201518160020160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550606082015181600301556080820151816004015560a0820151816005015560c0820151816006019081610ae09190611e33565b5060e08201518160070160006101000a81548160ff021916908315150217905550905050610b426040518060400160405280600f81526020017f332e204576656e742073746f7265640000000000000000000000000000000000815250610f19565b600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819080600181540180825580915050600190039060005260206000200160009091909190915055600080815480929190610bba90611f34565b9190505550610bfd6040518060400160405280601c81526020017f342e204f7267616e697a6572206d617070696e67207570646174656400000000815250610f19565b3373ffffffffffffffffffffffffffffffffffffffff16817fd05c332a5696a98df1d9668b66e5151cc308fc429c68bda2b0e70b09117f749f8a8a8a8a8a604051610c4c959493929190611fb8565b60405180910390a3610cca6040518060400160405280601681526020017f352e204576656e7420656d6974746564202d2049443a00000000000000000000815250826040518060400160405280600a81526020017f4f7267616e697a65723a000000000000000000000000000000000000000000008152503361104e565b979650505050505050565b610cdd611136565b6000600160008481526020019081526020016000206000015403610d36576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610d2d906118f5565b60405180910390fd5b600160008381526020019081526020016000206040518061010001604052908160008201548152602001600182018054610d6f90611878565b80601f0160208091040260200160405190810160405280929190818152602001828054610d9b90611878565b8015610de85780601f10610dbd57610100808354040283529160200191610de8565b820191906000526020600020905b815481529060010190602001808311610dcb57829003601f168201915b505050505081526020016002820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001600382015481526020016004820154815260200160058201548152602001600682018054610e7590611878565b80601f0160208091040260200160405190810160405280929190818152602001828054610ea190611878565b8015610eee5780601f10610ec357610100808354040283529160200191610eee565b820191906000526020600020905b815481529060010190602001808311610ed157829003601f168201915b505050505081526020016007820160009054906101000a900460ff1615151515815250509050919050565b610faf81604051602401610f2d9190612006565b6040516020818303038152906040527f41304fac000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506110f0565b50565b61104a8282604051602401610fc8929190612028565b6040516020818303038152906040527fb60e72cc000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506110f0565b5050565b6110ea848484846040516024016110689493929190612058565b6040516020818303038152906040527f7c4632a4000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506110f0565b50505050565b611107816110ff61110a61112b565b63ffffffff16565b50565b60006a636f6e736f6c652e6c6f679050600080835160208501845afa505050565b611193819050919050565b6040518061010001604052806000815260200160608152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600081526020016000815260200160008152602001606081526020016000151581525090565b61119b6120ab565b565b600080fd5b600080fd5b6000819050919050565b6111ba816111a7565b81146111c557600080fd5b50565b6000813590506111d7816111b1565b92915050565b6000602082840312156111f3576111f261119d565b5b6000611201848285016111c8565b91505092915050565b611213816111a7565b82525050565b600081519050919050565b600082825260208201905092915050565b60005b83811015611253578082015181840152602081019050611238565b60008484015250505050565b6000601f19601f8301169050919050565b600061127b82611219565b6112858185611224565b9350611295818560208601611235565b61129e8161125f565b840191505092915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60006112d4826112a9565b9050919050565b6112e4816112c9565b82525050565b60008115159050919050565b6112ff816112ea565b82525050565b60006101008201905061131b600083018b61120a565b818103602083015261132d818a611270565b905061133c60408301896112db565b611349606083018861120a565b611356608083018761120a565b61136360a083018661120a565b81810360c08301526113758185611270565b905061138460e08301846112f6565b9998505050505050505050565b60006020820190506113a660008301846112db565b92915050565b600080fd5b600080fd5b600080fd5b60008083601f8401126113d1576113d06113ac565b5b8235905067ffffffffffffffff8111156113ee576113ed6113b1565b5b60208301915083600182028301111561140a576114096113b6565b5b9250929050565b6000806000806000806080878903121561142e5761142d61119d565b5b600061143c89828a016111c8565b965050602087013567ffffffffffffffff81111561145d5761145c6111a2565b5b61146989828a016113bb565b9550955050604061147c89828a016111c8565b935050606087013567ffffffffffffffff81111561149d5761149c6111a2565b5b6114a989828a016113bb565b92509250509295509295509295565b6114c1816112c9565b81146114cc57600080fd5b50565b6000813590506114de816114b8565b92915050565b6000602082840312156114fa576114f961119d565b5b6000611508848285016114cf565b91505092915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b611546816111a7565b82525050565b6000611558838361153d565b60208301905092915050565b6000602082019050919050565b600061157c82611511565b611586818561151c565b93506115918361152d565b8060005b838110156115c25781516115a9888261154c565b97506115b483611564565b925050600181019050611595565b5085935050505092915050565b600060208201905081810360008301526115e98184611571565b905092915050565b6000602082019050611606600083018461120a565b92915050565b600080604083850312156116235761162261119d565b5b6000611631858286016114cf565b9250506020611642858286016111c8565b9150509250929050565b600080600080600080600060a0888a03121561166b5761166a61119d565b5b600088013567ffffffffffffffff811115611689576116886111a2565b5b6116958a828b016113bb565b975097505060206116a88a828b016111c8565b95505060406116b98a828b016111c8565b94505060606116ca8a828b016111c8565b935050608088013567ffffffffffffffff8111156116eb576116ea6111a2565b5b6116f78a828b016113bb565b925092505092959891949750929550565b600082825260208201905092915050565b600061172482611219565b61172e8185611708565b935061173e818560208601611235565b6117478161125f565b840191505092915050565b61175b816112c9565b82525050565b61176a816112ea565b82525050565b600061010083016000830151611789600086018261153d565b50602083015184820360208601526117a18282611719565b91505060408301516117b66040860182611752565b5060608301516117c9606086018261153d565b5060808301516117dc608086018261153d565b5060a08301516117ef60a086018261153d565b5060c083015184820360c08601526118078282611719565b91505060e083015161181c60e0860182611761565b508091505092915050565b600060208201905081810360008301526118418184611770565b905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b6000600282049050600182168061189057607f821691505b6020821081036118a3576118a2611849565b5b50919050565b7f4576656e7420646f6573206e6f74206578697374000000000000000000000000600082015250565b60006118df601483611224565b91506118ea826118a9565b602082019050919050565b6000602082019050818103600083015261190e816118d2565b9050919050565b7f4e6f7420617574686f72697a6564000000000000000000000000000000000000600082015250565b600061194b600e83611224565b915061195682611915565b602082019050919050565b6000602082019050818103600083015261197a8161193e565b9050919050565b600082905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b60008190508160005260206000209050919050565b60006020601f8301049050919050565b600082821b905092915050565b600060088302611a1d7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff826119e0565b611a2786836119e0565b95508019841693508086168417925050509392505050565b6000819050919050565b6000611a64611a5f611a5a846111a7565b611a3f565b6111a7565b9050919050565b6000819050919050565b611a7e83611a49565b611a92611a8a82611a6b565b8484546119ed565b825550505050565b600090565b611aa7611a9a565b611ab2818484611a75565b505050565b5b81811015611ad657611acb600082611a9f565b600181019050611ab8565b5050565b601f821115611b1b57611aec816119bb565b611af5846119d0565b81016020851015611b04578190505b611b18611b10856119d0565b830182611ab7565b50505b505050565b600082821c905092915050565b6000611b3e60001984600802611b20565b1980831691505092915050565b6000611b578383611b2d565b9150826002028217905092915050565b611b718383611981565b67ffffffffffffffff811115611b8a57611b8961198c565b5b611b948254611878565b611b9f828285611ada565b6000601f831160018114611bce5760008415611bbc578287013590505b611bc68582611b4b565b865550611c2e565b601f198416611bdc866119bb565b60005b82811015611c0457848901358255600182019150602085019450602081019050611bdf565b86831015611c215784890135611c1d601f891682611b2d565b8355505b6001600288020188555050505b50505050505050565b7f4576656e74206e616d652063616e6e6f7420626520656d707479000000000000600082015250565b6000611c6d601a83611224565b9150611c7882611c37565b602082019050919050565b60006020820190508181036000830152611c9c81611c60565b9050919050565b7f4576656e742064617465206d75737420626520696e2074686520667574757265600082015250565b6000611cd9602083611224565b9150611ce482611ca3565b602082019050919050565b60006020820190508181036000830152611d0881611ccc565b9050919050565b7f546f74616c20737570706c79206d75737420626520677265617465722074686160008201527f6e20300000000000000000000000000000000000000000000000000000000000602082015250565b6000611d6b602383611224565b9150611d7682611d0f565b604082019050919050565b60006020820190508181036000830152611d9a81611d5e565b9050919050565b7f5469636b6574207072696365206d75737420626520677265617465722074686160008201527f6e20300000000000000000000000000000000000000000000000000000000000602082015250565b6000611dfd602383611224565b9150611e0882611da1565b604082019050919050565b60006020820190508181036000830152611e2c81611df0565b9050919050565b611e3c82611219565b67ffffffffffffffff811115611e5557611e5461198c565b5b611e5f8254611878565b611e6a828285611ada565b600060209050601f831160018114611e9d5760008415611e8b578287015190505b611e958582611b4b565b865550611efd565b601f198416611eab866119bb565b60005b82811015611ed357848901518255600182019150602085019450602081019050611eae565b86831015611ef05784890151611eec601f891682611b2d565b8355505b6001600288020188555050505b505050505050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b6000611f3f826111a7565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8203611f7157611f70611f05565b5b600182019050919050565b82818337600083830152505050565b6000611f978385611224565b9350611fa4838584611f7c565b611fad8361125f565b840190509392505050565b60006080820190508181036000830152611fd3818789611f8b565b9050611fe2602083018661120a565b611fef604083018561120a565b611ffc606083018461120a565b9695505050505050565b600060208201905081810360008301526120208184611270565b905092915050565b600060408201905081810360008301526120428185611270565b9050612051602083018461120a565b9392505050565b600060808201905081810360008301526120728187611270565b9050612081602083018661120a565b81810360408301526120938185611270565b90506120a260608301846112db565b95945050505050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052605160045260246000fdfea2646970667358221220467ecfebc321947cf0d0148eb131cd18aaef7ce04be61efca1271a19d248dad064736f6c634300081c0033";

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
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
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
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(EVENTCREATED_EVENT, transactionReceipt);
        ArrayList<EventCreatedEventResponse> responses = new ArrayList<EventCreatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            EventCreatedEventResponse typedResponse = new EventCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.eventId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.organizer = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.eventDate = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.totalSupply = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.ticketPrice = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static EventCreatedEventResponse getEventCreatedEventFromLog(Log log) {
        EventValuesWithLog eventValues = staticExtractEventParametersWithLog(EVENTCREATED_EVENT, log);
        EventCreatedEventResponse typedResponse = new EventCreatedEventResponse();
        typedResponse.log = log;
        typedResponse.eventId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.organizer = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.eventDate = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.totalSupply = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.ticketPrice = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
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

    public RemoteFunctionCall<TransactionReceipt> createEvent(String _name, BigInteger _eventDate,
            BigInteger _totalSupply, BigInteger _ticketPrice, String _metadataURI) {
        final Function function = new Function(
                FUNC_CREATEEVENT, 
                Arrays.<Type>asList(new Utf8String(_name),
                new Uint256(_eventDate),
                new Uint256(_totalSupply),
                new Uint256(_ticketPrice),
                new Utf8String(_metadataURI)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> deactivateEvent(BigInteger _eventId) {
        final Function function = new Function(
                FUNC_DEACTIVATEEVENT, 
                Arrays.<Type>asList(new Uint256(_eventId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple8<BigInteger, String, String, BigInteger, BigInteger, BigInteger, String, Boolean>> events(
            BigInteger param0) {
        final Function function = new Function(FUNC_EVENTS, 
                Arrays.<Type>asList(new Uint256(param0)),
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
                Arrays.<Type>asList(new Address(160, param0),
                new Uint256(param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Event> getEventDetails(BigInteger _eventId) {
        final Function function = new Function(FUNC_GETEVENTDETAILS, 
                Arrays.<Type>asList(new Uint256(_eventId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Event>() {}));
        return executeRemoteCallSingleValueReturn(function, Event.class);
    }

    public RemoteFunctionCall<List> getEventsByOrganizer(String _organizer) {
        final Function function = new Function(FUNC_GETEVENTSBYORGANIZER, 
                Arrays.<Type>asList(new Address(160, _organizer)),
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
                Arrays.<Type>asList(new Uint256(_eventId),
                new Utf8String(_name),
                new Uint256(_eventDate),
                new Utf8String(_metadataURI)),
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

    public static void linkLibraries(List<LinkReference> references) {
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
            super(new Uint256(id),
                    new Utf8String(name),
                    new Address(160, organizer),
                    new Uint256(eventDate),
                    new Uint256(totalSupply),
                    new Uint256(ticketPrice),
                    new Utf8String(metadataURI),
                    new Bool(isActive));
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
    }
}
