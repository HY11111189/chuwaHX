// Q11
class Student {
    private String name;
    private int age;
    private double grade;

    public Student(String name, int age, double grade) {
        this.name = name;
        setAge(age);
        setGrade(grade);
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public double getGrade() { return grade; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) {
        if(age >= 1 && age <= 150) this.age = age;
    }
    public void setGrade(double grade) {
        if(grade >= 0.0 && grade <= 100.0) this.grade = grade;
    }

    public static void main(String[] args) {
        Student s = new Student("Alice", 15, 97);
        System.out.println(s.getName() + " " + s.getAge() + " " + s.getGrade());
        s.setAge(161); // invalid input, will not be set
        s.setGrade(-1); // invalid, will not be set
        System.out.println(s.getAge() + " " + s.getGrade());
    }
}

// Q12
class BankAccount {
    private String accountNumber;
    private double balance;

    public BankAccount(String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = 0.0;
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }

    public boolean deposit(double amount) {
        if(amount > 0) {
            balance += amount;
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount) {
        if(amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        BankAccount account = new BankAccount("11111");
        System.out.println(account.deposit(105));  // true
        System.out.println(account.withdraw(5));  // true
        System.out.println(account.deposit(-1));  // false
        System.out.println(account.withdraw(1000)); // false
        System.out.println(account.getBalance());  // 100.0
    }
}