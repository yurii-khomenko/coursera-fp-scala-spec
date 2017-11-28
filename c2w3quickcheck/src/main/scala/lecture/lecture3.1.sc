class BankAccount {

  private var balance = 0

  def deposit(amount: Int): Unit = {
    if (amount > 0) balance += amount
  }

  def withdraw(amount: Int): Int =
    if (0 < amount && amount <= balance) {
      balance -= amount
      balance
    } else throw new Error("insufficient funds")
}


val acc = new BankAccount
acc deposit 50
acc withdraw 20
acc withdraw 20
acc withdraw 15
