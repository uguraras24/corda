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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@StartableByRPC
@StartableByService
public class ViewInboxByAccount extends FlowLogic<List<String>>{

    private final String acctName;

    public ViewInboxByAccount(String acctname) {
        this.acctName = acctname;
    }

    @Override
    public List<String> call() throws FlowException {

        AccountService accountService = getServiceHub().cordaService(KeyManagementBackedAccountService.class);
        AccountInfo myAccount = accountService.accountInfo(acctName).get(0).getState().getData();
        QueryCriteria.VaultQueryCriteria criteria = new QueryCriteria.VaultQueryCriteria()
                .withExternalIds(Arrays.asList(myAccount.getIdentifier().getId()));

        List<String> InternalMessages = getServiceHub().getVaultService().queryBy(InternalMessageState.class,criteria).getStates().stream().map(
                it -> "\nInternalMessages State : " + it.getState().getData().getTask()).collect(Collectors.toList());

        List<String> payments = getServiceHub().getVaultService().queryBy(PaymentState.class,criteria).getStates().stream().map(
                it -> "\nPayment : " +it.getState().getData().getAmount()).collect(Collectors.toList());
        List<String> month = getServiceHub().getVaultService().queryBy(PaymentState.class,criteria).getStates().stream().map(
                it -> "\nMonth : " +it.getState().getData().getMonth()).collect(Collectors.toList());
        List<String> distribution = getServiceHub().getVaultService().queryBy(PaymentState.class,criteria).getStates().stream().map(
                it -> "\nDistribution : " +it.getState().getData().getDistribution()).collect(Collectors.toList());
        List<String> fund = getServiceHub().getVaultService().queryBy(PaymentState.class,criteria).getStates().stream().map(
                it -> "\nFund : " +it.getState().getData().getFundid()).collect(Collectors.toList());


        return Stream.of(InternalMessages, payments,month,distribution,fund).flatMap(Collection::stream).collect(Collectors.toList());
    }
}