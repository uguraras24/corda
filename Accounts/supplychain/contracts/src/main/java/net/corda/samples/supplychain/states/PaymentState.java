package net.corda.samples.supplychain.states;

import net.corda.samples.supplychain.contracts.PaymentStateContract;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.AnonymousParty;

import java.util.ArrayList;
import java.util.List;

// *********
// * State *
// *********
@BelongsToContract(PaymentStateContract.class)
public class PaymentState implements ContractState {

    private int amount;
    private int rate;
    private String month;
    private int distribution;
    private String fundid;
    private AnonymousParty sender;
    private AnonymousParty recipient;
    private List<AbstractParty> participants;

    public PaymentState(int amount,int rate,String month,int distribution, String fundid, AnonymousParty sender, AnonymousParty recipient) {
        this.amount = amount;
        this.rate=rate;
        this.month=month;
        this.distribution=distribution;
        this.fundid=fundid;
        this.sender = sender;
        this.recipient = recipient;
        this.participants = new ArrayList<AbstractParty>();
        participants.add(sender);
        participants.add(recipient);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public int  getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }


    public String getMonth() {
        return month;
    }

    public void setMonth(String month) { this.month = month; }


    public int  getDistribution() {
        return distribution;
    }

    public void setDistribution(int distribution) {
        this.distribution = distribution;
    }


    public String getFundid() {
        return fundid;
    }

    public void setFundid(String fundid) { this.fundid = fundid; }


    public AnonymousParty getSender() {
        return sender;
    }

    public void setSender(AnonymousParty sender) {
        this.sender = sender;
    }

    public AnonymousParty getRecipient() {
        return recipient;
    }

    public void setRecipient(AnonymousParty recipient) {
        this.recipient = recipient;
    }

    @Override
    public List<AbstractParty> getParticipants() {
        return this.participants;
    }
}