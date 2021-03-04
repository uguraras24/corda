package net.corda.samples.supplychain.accountUtilities;

import com.r3.corda.lib.accounts.contracts.states.AccountInfo;
import com.r3.corda.lib.accounts.workflows.services.AccountService;
import com.r3.corda.lib.accounts.workflows.services.KeyManagementBackedAccountService;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.flows.StartableByService;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.samples.supplychain.states.*;
import net.corda.core.contracts.StateAndRef;
import java.util.*;
import java.util.stream.Collectors;
import org.json.simple.*;

@StartableByRPC
@StartableByService
public class ViewInboxByAccount extends FlowLogic<List<String>> {

    private final String acctName;

    public ViewInboxByAccount(String acctname) {
        this.acctName = acctname;
    }

    @Override
    public List<String> call() throws FlowException {

        List<String> flowlist = new ArrayList<String>();

        List<String> payments = new ArrayList<String>();
        List<String> month = new ArrayList<String>();
        List<String> distribution = new ArrayList<String>();
        List<String> fund = new ArrayList<String>();

        AccountService accountService = getServiceHub().cordaService(KeyManagementBackedAccountService.class);
        AccountInfo myAccount = accountService.accountInfo(acctName).get(0).getState().getData();

        QueryCriteria.VaultQueryCriteria criteria = new QueryCriteria.VaultQueryCriteria()
                .withExternalIds(Arrays.asList(myAccount.getIdentifier().getId()));

        payments = getServiceHub().getVaultService().queryBy(PaymentState.class, criteria).getStates().stream().map(
                it -> "" + it.getState().getData().getAmount()).collect(Collectors.toList());
        month = getServiceHub().getVaultService().queryBy(PaymentState.class, criteria).getStates().stream().map(
                it -> "" + it.getState().getData().getMonth()).collect(Collectors.toList());
        distribution = getServiceHub().getVaultService().queryBy(PaymentState.class, criteria).getStates().stream().map(
                it -> "" + it.getState().getData().getDistribution()).collect(Collectors.toList());
        fund = getServiceHub().getVaultService().queryBy(PaymentState.class, criteria).getStates().stream().map(
                it -> "" + it.getState().getData().getFundid()).collect(Collectors.toList());

        for(int i=0;i<payments.size();i++){
            JSONObject flow = new JSONObject();
            flow.put("payment",payments.get(i));
            flow.put("month",month.get(i));
            flow.put("distribution",distribution.get(i));
            flow.put("fundid",fund.get(i));
            flowlist.add(flow.toString());
        }
        return flowlist;
    }
}