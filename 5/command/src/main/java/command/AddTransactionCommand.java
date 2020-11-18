package command;

import model.Account;
import model.Transaction;

public class AddTransactionCommand implements Command {
    private Transaction transactionToAdd;
    private Account account;

    public AddTransactionCommand(Transaction transactionToAdd, Account account) {
        this.transactionToAdd = transactionToAdd;
        this.account = account;
    }

    @Override
    public void undo(){
        account.removeTransaction(transactionToAdd);
    }

    @Override
    public void redo(){
        execute();
    }

    @Override
    public void execute() {
        account.addTransaction(transactionToAdd);
    }

    @Override
    public String getName() {
        return "New transaction: " + transactionToAdd.toString();
    }
}
