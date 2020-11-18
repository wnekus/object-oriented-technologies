package command;

import model.Account;
import model.Transaction;

public class RemoveTransactionCommand implements Command {
    private Transaction transactionToRemove;
    private Account account;

    public RemoveTransactionCommand(Transaction transactionToRemove, Account account) {
        this.transactionToRemove = transactionToRemove;
        this.account = account;
    }

    @Override
    public void undo(){
        account.addTransaction(transactionToRemove);
    }

    @Override
    public void redo(){
        execute();
    }

    @Override
    public void execute() {
        account.removeTransaction(transactionToRemove);
    }

    @Override
    public String getName() {
        return "Removed transaction: " + transactionToRemove.toString();
    }
}
