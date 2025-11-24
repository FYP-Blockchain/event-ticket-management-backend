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
    public static final String BINARY = "6080604052348015600f57600080fd5b5033600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600160008190555061260b806100686000396000f3fe608060405234801561001057600080fd5b50600436106100ea5760003560e01c80639f9d903a1161008c578063d248367711610066578063d24836771461023e578063d78741e31461025a578063e6ba26901461028a578063ec38d5a0146102ba576100ea565b80639f9d903a146101e6578063a69fb9d814610204578063c7b6084c14610220576100ea565b80638da5cb5b116100c85780638da5cb5b14610160578063957a632e1461017e5780639cb3c7061461019a5780639f6271c3146101b6576100ea565b806302262ced146100ef5780630b7914301461010b57806330c7b45f14610142575b600080fd5b610109600480360381019061010491906114d6565b6102ea565b005b61012560048036038101906101209190611539565b61042d565b60405161013998979695949392919061162f565b60405180910390f35b61014a6105b2565b60405161015791906116bb565b60405180910390f35b6101686105d8565b60405161017591906116bb565b60405180910390f35b61019860048036038101906101939190611539565b6105fe565b005b6101b460048036038101906101af91906114d6565b6107b8565b005b6101d060048036038101906101cb91906114d6565b6108fb565b6040516101dd9190611794565b60405180910390f35b6101ee610992565b6040516101fb91906117b6565b60405180910390f35b61021e60048036038101906102199190611539565b610998565b005b610228610beb565b60405161023591906116bb565b60405180910390f35b61025860048036038101906102539190611836565b610c11565b005b610274600480360381019061026f9190611905565b610df9565b60405161028191906117b6565b60405180910390f35b6102a4600480360381019061029f9190611945565b610e2a565b6040516102b191906117b6565b60405180910390f35b6102d460048036038101906102cf9190611539565b6111cd565b6040516102e19190611b20565b60405180910390f35b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461037a576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161037190611bb4565b60405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16036103e9576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016103e090611c20565b60405180910390fd5b80600560006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b600160205280600052604060002060009150905080600001549080600101805461045690611c6f565b80601f016020809104026020016040519081016040528092919081815260200182805461048290611c6f565b80156104cf5780601f106104a4576101008083540402835291602001916104cf565b820191906000526020600020905b8154815290600101906020018083116104b257829003601f168201915b5050505050908060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169080600301549080600401549080600501549080600601805461051c90611c6f565b80601f016020809104026020016040519081016040528092919081815260200182805461054890611c6f565b80156105955780601f1061056a57610100808354040283529160200191610595565b820191906000526020600020905b81548152906001019060200180831161057857829003601f168201915b5050505050908060070160009054906101000a900460ff16905088565b600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000600160008381526020019081526020016000209050600060016000848152602001908152602001600020600001540361066e576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161066590611cec565b60405180910390fd5b6001600083815260200190815260200160002060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16148061072b5750600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b61076a576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161076190611d58565b60405180910390fd5b60008160070160006101000a81548160ff021916908315150217905550817f9bec5b77a290657edefc9defa075c60a874e90f7cc9496853ca9962d58a4ffdd60405160405180910390a25050565b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610848576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161083f90611bb4565b60405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16036108b7576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016108ae90611dc4565b60405180910390fd5b80600460006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b6060600260008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002080548060200260200160405190810160405280929190818152602001828054801561098657602002820191906000526020600020905b815481526020019060010190808311610972575b50505050509050919050565b60005481565b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161480610a415750600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b80610a995750600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b610ad8576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610acf90611e30565b60405180910390fd5b60006001600083815260200190815260200160002090506000600160008481526020019081526020016000206000015403610b48576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610b3f90611cec565b60405180910390fd5b6000816004015411610b8f576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610b8690611e9c565b60405180910390fd5b6001816004016000828254610ba49190611eeb565b92505081905550817fefeeb73320087a2a47bf61966e558f721b92abd63ce46882b16691e6ae9dee8c8260040154604051610bdf91906117b6565b60405180910390a25050565b600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000600160008a815260200190815260200160002090506000600160008b81526020019081526020016000206000015403610c81576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610c7890611cec565b60405180910390fd5b8060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161480610d2c5750600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b610d6b576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610d6290611d58565b60405180910390fd5b8787826001019182610d7e929190612105565b508581600301819055508484826006019182610d9b929190612105565b50828160040181905550818160050181905550887fa50dbbdbe33d560afa7865f7dfb02e4856fb0d965cba37c505970776b4be13968989898989604051610de6959493929190612211565b60405180910390a2505050505050505050565b60026020528160005260406000208181548110610e1557600080fd5b90600052602060002001600091509150505481565b6000808888905011610e71576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610e68906122a6565b60405180910390fd5b428611610eb3576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610eaa90612312565b60405180910390fd5b60008511610ef6576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610eed906123a4565b60405180910390fd5b60008411610f39576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610f3090612436565b60405180910390fd5b600054905060405180610100016040528082815260200189898080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505081526020013373ffffffffffffffffffffffffffffffffffffffff16815260200187815260200186815260200185815260200184848080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050508152602001600115158152506001600083815260200190815260200160002060008201518160000155602082015181600101908161104b9190612456565b5060408201518160020160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550606082015181600301556080820151816004015560a0820151816005015560c08201518160060190816110c69190612456565b5060e08201518160070160006101000a81548160ff021916908315150217905550905050600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081908060018154018082558091505060019003906000526020600020016000909190919091505560008081548092919061116290612528565b91905055503373ffffffffffffffffffffffffffffffffffffffff16817fcef15f0f10c2f2d51dbb99745c47fabc9591ec875a1d26bdc5e0c2f1e131c4f18a8a8a8a8a8a8a6040516111ba9796959493929190612570565b60405180910390a3979650505050505050565b6111d5611411565b600060016000848152602001908152602001600020600001540361122e576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161122590611cec565b60405180910390fd5b60016000838152602001908152602001600020604051806101000160405290816000820154815260200160018201805461126790611c6f565b80601f016020809104026020016040519081016040528092919081815260200182805461129390611c6f565b80156112e05780601f106112b5576101008083540402835291602001916112e0565b820191906000526020600020905b8154815290600101906020018083116112c357829003601f168201915b505050505081526020016002820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200160038201548152602001600482015481526020016005820154815260200160068201805461136d90611c6f565b80601f016020809104026020016040519081016040528092919081815260200182805461139990611c6f565b80156113e65780601f106113bb576101008083540402835291602001916113e6565b820191906000526020600020905b8154815290600101906020018083116113c957829003601f168201915b505050505081526020016007820160009054906101000a900460ff1615151515815250509050919050565b6040518061010001604052806000815260200160608152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600081526020016000815260200160008152602001606081526020016000151581525090565b600080fd5b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60006114a382611478565b9050919050565b6114b381611498565b81146114be57600080fd5b50565b6000813590506114d0816114aa565b92915050565b6000602082840312156114ec576114eb61146e565b5b60006114fa848285016114c1565b91505092915050565b6000819050919050565b61151681611503565b811461152157600080fd5b50565b6000813590506115338161150d565b92915050565b60006020828403121561154f5761154e61146e565b5b600061155d84828501611524565b91505092915050565b61156f81611503565b82525050565b600081519050919050565b600082825260208201905092915050565b60005b838110156115af578082015181840152602081019050611594565b60008484015250505050565b6000601f19601f8301169050919050565b60006115d782611575565b6115e18185611580565b93506115f1818560208601611591565b6115fa816115bb565b840191505092915050565b61160e81611498565b82525050565b60008115159050919050565b61162981611614565b82525050565b600061010082019050611645600083018b611566565b8181036020830152611657818a6115cc565b90506116666040830189611605565b6116736060830188611566565b6116806080830187611566565b61168d60a0830186611566565b81810360c083015261169f81856115cc565b90506116ae60e0830184611620565b9998505050505050505050565b60006020820190506116d06000830184611605565b92915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b61170b81611503565b82525050565b600061171d8383611702565b60208301905092915050565b6000602082019050919050565b6000611741826116d6565b61174b81856116e1565b9350611756836116f2565b8060005b8381101561178757815161176e8882611711565b975061177983611729565b92505060018101905061175a565b5085935050505092915050565b600060208201905081810360008301526117ae8184611736565b905092915050565b60006020820190506117cb6000830184611566565b92915050565b600080fd5b600080fd5b600080fd5b60008083601f8401126117f6576117f56117d1565b5b8235905067ffffffffffffffff811115611813576118126117d6565b5b60208301915083600182028301111561182f5761182e6117db565b5b9250929050565b60008060008060008060008060c0898b0312156118565761185561146e565b5b60006118648b828c01611524565b985050602089013567ffffffffffffffff81111561188557611884611473565b5b6118918b828c016117e0565b975097505060406118a48b828c01611524565b955050606089013567ffffffffffffffff8111156118c5576118c4611473565b5b6118d18b828c016117e0565b945094505060806118e48b828c01611524565b92505060a06118f58b828c01611524565b9150509295985092959890939650565b6000806040838503121561191c5761191b61146e565b5b600061192a858286016114c1565b925050602061193b85828601611524565b9150509250929050565b600080600080600080600060a0888a0312156119645761196361146e565b5b600088013567ffffffffffffffff81111561198257611981611473565b5b61198e8a828b016117e0565b975097505060206119a18a828b01611524565b95505060406119b28a828b01611524565b94505060606119c38a828b01611524565b935050608088013567ffffffffffffffff8111156119e4576119e3611473565b5b6119f08a828b016117e0565b925092505092959891949750929550565b600082825260208201905092915050565b6000611a1d82611575565b611a278185611a01565b9350611a37818560208601611591565b611a40816115bb565b840191505092915050565b611a5481611498565b82525050565b611a6381611614565b82525050565b600061010083016000830151611a826000860182611702565b5060208301518482036020860152611a9a8282611a12565b9150506040830151611aaf6040860182611a4b565b506060830151611ac26060860182611702565b506080830151611ad56080860182611702565b5060a0830151611ae860a0860182611702565b5060c083015184820360c0860152611b008282611a12565b91505060e0830151611b1560e0860182611a5a565b508091505092915050565b60006020820190508181036000830152611b3a8184611a69565b905092915050565b7f4576656e744d616e616765723a2043616c6c6572206973206e6f74207468652060008201527f6f776e6572000000000000000000000000000000000000000000000000000000602082015250565b6000611b9e602583611580565b9150611ba982611b42565b604082019050919050565b60006020820190508181036000830152611bcd81611b91565b9050919050565b7f496e76616c6964206f70657261746f7220616464726573730000000000000000600082015250565b6000611c0a601883611580565b9150611c1582611bd4565b602082019050919050565b60006020820190508181036000830152611c3981611bfd565b9050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b60006002820490506001821680611c8757607f821691505b602082108103611c9a57611c99611c40565b5b50919050565b7f4576656e7420646f6573206e6f74206578697374000000000000000000000000600082015250565b6000611cd6601483611580565b9150611ce182611ca0565b602082019050919050565b60006020820190508181036000830152611d0581611cc9565b9050919050565b7f4e6f7420617574686f72697a6564000000000000000000000000000000000000600082015250565b6000611d42600e83611580565b9150611d4d82611d0c565b602082019050919050565b60006020820190508181036000830152611d7181611d35565b9050919050565b7f496e76616c696420636f6e747261637420616464726573730000000000000000600082015250565b6000611dae601883611580565b9150611db982611d78565b602082019050919050565b60006020820190508181036000830152611ddd81611da1565b9050919050565b7f4576656e744d616e616765723a204e6f7420617574686f72697a656400000000600082015250565b6000611e1a601c83611580565b9150611e2582611de4565b602082019050919050565b60006020820190508181036000830152611e4981611e0d565b9050919050565b7f4e6f207469636b65747320617661696c61626c65000000000000000000000000600082015250565b6000611e86601483611580565b9150611e9182611e50565b602082019050919050565b60006020820190508181036000830152611eb581611e79565b9050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b6000611ef682611503565b9150611f0183611503565b9250828203905081811115611f1957611f18611ebc565b5b92915050565b600082905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b60008190508160005260206000209050919050565b60006020601f8301049050919050565b600082821b905092915050565b600060088302611fbb7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82611f7e565b611fc58683611f7e565b95508019841693508086168417925050509392505050565b6000819050919050565b6000612002611ffd611ff884611503565b611fdd565b611503565b9050919050565b6000819050919050565b61201c83611fe7565b61203061202882612009565b848454611f8b565b825550505050565b600090565b612045612038565b612050818484612013565b505050565b5b818110156120745761206960008261203d565b600181019050612056565b5050565b601f8211156120b95761208a81611f59565b61209384611f6e565b810160208510156120a2578190505b6120b66120ae85611f6e565b830182612055565b50505b505050565b600082821c905092915050565b60006120dc600019846008026120be565b1980831691505092915050565b60006120f583836120cb565b9150826002028217905092915050565b61210f8383611f1f565b67ffffffffffffffff81111561212857612127611f2a565b5b6121328254611c6f565b61213d828285612078565b6000601f83116001811461216c576000841561215a578287013590505b61216485826120e9565b8655506121cc565b601f19841661217a86611f59565b60005b828110156121a25784890135825560018201915060208501945060208101905061217d565b868310156121bf57848901356121bb601f8916826120cb565b8355505b6001600288020188555050505b50505050505050565b82818337600083830152505050565b60006121f08385611580565b93506121fd8385846121d5565b612206836115bb565b840190509392505050565b6000606082019050818103600083015261222c8187896121e4565b905061223b6020830186611566565b818103604083015261224e8184866121e4565b90509695505050505050565b7f4576656e74206e616d652063616e6e6f7420626520656d707479000000000000600082015250565b6000612290601a83611580565b915061229b8261225a565b602082019050919050565b600060208201905081810360008301526122bf81612283565b9050919050565b7f4576656e742064617465206d75737420626520696e2074686520667574757265600082015250565b60006122fc602083611580565b9150612307826122c6565b602082019050919050565b6000602082019050818103600083015261232b816122ef565b9050919050565b7f546f74616c20737570706c79206d75737420626520677265617465722074686160008201527f6e20300000000000000000000000000000000000000000000000000000000000602082015250565b600061238e602383611580565b915061239982612332565b604082019050919050565b600060208201905081810360008301526123bd81612381565b9050919050565b7f5469636b6574207072696365206d75737420626520677265617465722074686160008201527f6e20300000000000000000000000000000000000000000000000000000000000602082015250565b6000612420602383611580565b915061242b826123c4565b604082019050919050565b6000602082019050818103600083015261244f81612413565b9050919050565b61245f82611575565b67ffffffffffffffff81111561247857612477611f2a565b5b6124828254611c6f565b61248d828285612078565b600060209050601f8311600181146124c057600084156124ae578287015190505b6124b885826120e9565b865550612520565b601f1984166124ce86611f59565b60005b828110156124f6578489015182556001820191506020850194506020810190506124d1565b86831015612513578489015161250f601f8916826120cb565b8355505b6001600288020188555050505b505050505050565b600061253382611503565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff820361256557612564611ebc565b5b600182019050919050565b600060a082019050818103600083015261258b81898b6121e4565b905061259a6020830188611566565b6125a76040830187611566565b6125b46060830186611566565b81810360808301526125c78184866121e4565b90509897505050505050505056fea264697066735822122013a184576d3e9ebff584fa596137fb546445f2ae516dbbf015901a1114a98ff464736f6c634300081c0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_BACKENDOPERATOR = "backendOperator";

    public static final String FUNC_CREATEEVENT = "createEvent";

    public static final String FUNC_DEACTIVATEEVENT = "deactivateEvent";

    public static final String FUNC_DECREMENTTICKETSUPPLY = "decrementTicketSupply";

    public static final String FUNC_EVENTS = "events";

    public static final String FUNC_EVENTSBYORGANIZER = "eventsByOrganizer";

    public static final String FUNC_GETEVENTDETAILS = "getEventDetails";

    public static final String FUNC_GETEVENTSBYORGANIZER = "getEventsByOrganizer";

    public static final String FUNC_NEXTEVENTID = "nextEventId";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_SETBACKENDOPERATOR = "setBackendOperator";

    public static final String FUNC_SETTICKETNFTCONTRACT = "setTicketNFTContract";

    public static final String FUNC_TICKETNFTCONTRACT = "ticketNFTContract";

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

    public static final org.web3j.abi.datatypes.Event TICKETSOLD_EVENT = new org.web3j.abi.datatypes.Event("TicketSold", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}));
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

    public static List<TicketSoldEventResponse> getTicketSoldEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(TICKETSOLD_EVENT, transactionReceipt);
        ArrayList<TicketSoldEventResponse> responses = new ArrayList<TicketSoldEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TicketSoldEventResponse typedResponse = new TicketSoldEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.eventId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.remainingSupply = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static TicketSoldEventResponse getTicketSoldEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(TICKETSOLD_EVENT, log);
        TicketSoldEventResponse typedResponse = new TicketSoldEventResponse();
        typedResponse.log = log;
        typedResponse.eventId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.remainingSupply = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<TicketSoldEventResponse> ticketSoldEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getTicketSoldEventFromLog(log));
    }

    public Flowable<TicketSoldEventResponse> ticketSoldEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TICKETSOLD_EVENT));
        return ticketSoldEventFlowable(filter);
    }

    public RemoteFunctionCall<String> backendOperator() {
        final Function function = new Function(FUNC_BACKENDOPERATOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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

    public RemoteFunctionCall<TransactionReceipt> decrementTicketSupply(BigInteger _eventId) {
        final Function function = new Function(
                FUNC_DECREMENTTICKETSUPPLY, 
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

    public RemoteFunctionCall<TransactionReceipt> setBackendOperator(String _backendOperator) {
        final Function function = new Function(
                FUNC_SETBACKENDOPERATOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _backendOperator)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setTicketNFTContract(String _ticketNFTContract) {
        final Function function = new Function(
                FUNC_SETTICKETNFTCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _ticketNFTContract)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> ticketNFTContract() {
        final Function function = new Function(FUNC_TICKETNFTCONTRACT, 
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

    public static class TicketSoldEventResponse extends BaseEventResponse {
        public BigInteger eventId;

        public BigInteger remainingSupply;
    }
}
