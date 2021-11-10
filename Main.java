package com.workattech.splitwise.models;

public class User {
    private String id;
    private String name;
    private String email;
    private String phone;

    public User(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
//Split.java
package com.workattech.splitwise.models.split;

import com.workattech.splitwise.models.User;

public abstract class Split {
    private User user;
    double amount;

    public Split(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
//EqualSplit.java
package com.workattech.splitwise.models.split;

import com.workattech.splitwise.models.User;

public class EqualSplit extends Split {

    public EqualSplit(User user) {
        super(user);
    }
}
//ExactSplit.java
package com.workattech.splitwise.models.split;

import com.workattech.splitwise.models.User;

public class ExactSplit extends Split {

    public ExactSplit(User user, double amount) {
        super(user);
        this.amount = amount;
    }
}
//PercentSplit.java
package com.workattech.splitwise.models.split;

import com.workattech.splitwise.models.User;

public class PercentSplit extends Split {
    double percent;

    public PercentSplit(User user, double percent) {
        super(user);
        this.percent = percent;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}
//ExpenseMetadata.java
package com.workattech.splitwise.models.expense;

public class ExpenseMetadata {
    private String name;
    private String imgUrl;
    private String notes;

    public ExpenseMetadata(String name, String imgUrl, String notes) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
//Expense.java
package com.workattech.splitwise.models.expense;

import com.workattech.splitwise.models.User;
import com.workattech.splitwise.models.split.Split;

import java.util.List;

public abstract class Expense {
    private String id;
    private double amount;
    private User paidBy;
    private List<Split> splits;
    private ExpenseMetadata metadata;

    public Expense(double amount, User paidBy, List<Split> splits, ExpenseMetadata metadata) {
        this.amount = amount;
        this.paidBy = paidBy;
        this.splits = splits;
        this.metadata = metadata;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(User paidBy) {
        this.paidBy = paidBy;
    }

    public List<Split> getSplits() {
        return splits;
    }

    public void setSplits(List<Split> splits) {
        this.splits = splits;
    }

    public ExpenseMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ExpenseMetadata metadata) {
        this.metadata = metadata;
    }

    public abstract boolean validate();
}
//EqualExpense.java
package com.workattech.splitwise.models.expense;

import com.workattech.splitwise.models.User;
import com.workattech.splitwise.models.split.EqualSplit;
import com.workattech.splitwise.models.split.Split;

import java.util.List;

public class EqualExpense extends Expense {
    public EqualExpense(double amount, User paidBy, List<Split> splits, ExpenseMetadata expenseMetadata) {
        super(amount, paidBy, splits, expenseMetadata);
    }

    @Override
    public boolean validate() {
        for (Split split : getSplits()) {
            if (!(split instanceof EqualSplit)) {
                return false;
            }
        }

        return true;
    }
}
//ExactExpense.java
package com.workattech.splitwise.models.expense;

import com.workattech.splitwise.models.User;
import com.workattech.splitwise.models.split.ExactSplit;
import com.workattech.splitwise.models.split.Split;

import java.util.List;

public class ExactExpense extends Expense {
    public ExactExpense(double amount, User paidBy, List<Split> splits, ExpenseMetadata expenseMetadata) {
        super(amount, paidBy, splits, expenseMetadata);
    }

    @Override
    public boolean validate() {
        for (Split split : getSplits()) {
            if (!(split instanceof ExactSplit)) {
                return false;
            }
        }

        double totalAmount = getAmount();
        double sumSplitAmount = 0;
        for (Split split : getSplits()) {
            ExactSplit exactSplit = (ExactSplit) split;
            sumSplitAmount += exactSplit.getAmount();
        }

        if (totalAmount != sumSplitAmount) {
            return false;
        }

        return true;
    }
}
//PercentExpense.java
package com.workattech.splitwise.models.expense;

import com.workattech.splitwise.models.User;
import com.workattech.splitwise.models.split.PercentSplit;
import com.workattech.splitwise.models.split.Split;

import java.util.List;

public class PercentExpense extends Expense {
    public PercentExpense(double amount, User paidBy, List<Split> splits, ExpenseMetadata expenseMetadata) {
        super(amount, paidBy, splits, expenseMetadata);
    }

    @Override
    public boolean validate() {
        for (Split split : getSplits()) {
            if (!(split instanceof PercentSplit)) {
                return false;
            }
        }

        double totalPercent = 100;
        double sumSplitPercent = 0;
        for (Split split : getSplits()) {
            PercentSplit exactSplit = (PercentSplit) split;
            sumSplitPercent += exactSplit.getPercent();
        }

        if (totalPercent != sumSplitPercent) {
            return false;
        }

        return true;
    }
}
//ExpenseType.java
package com.workattech.splitwise.models.expense;

public enum ExpenseType {
    EQUAL,
    EXACT,
    PERCENT
}



