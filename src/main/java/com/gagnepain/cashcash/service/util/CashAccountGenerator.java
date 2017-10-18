package com.gagnepain.cashcash.service.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gagnepain.cashcash.domain.CashAccount;
import com.gagnepain.cashcash.domain.CashCurrency;
import com.gagnepain.cashcash.domain.User;
import com.gagnepain.cashcash.domain.enumeration.CashAccountType;

public final class CashAccountGenerator {
	public static Collection<CashAccount> generateInitialAccounts(final User loggedUser, final CashCurrency cashCurrency) {
		final Map<String, CashAccount> rootAccountsMap = new HashMap<>();
		for (final CashAccountType accountType : CashAccountType.values()) {
			final CashAccount cashAccount = new CashAccountBuilder().name(accountType.name())
					.type(accountType)
					.user(loggedUser)
					.currency(cashCurrency)
					.build();
			rootAccountsMap.put(accountType.name(), cashAccount);
		}

		// We will create a default structure
		//
		List<CashAccount> cashAccountList = new ArrayList<>();
		cashAccountList.addAll(rootAccountsMap.values());

		// ASSET
		// |__ Current Assets
		// |    |__ Default Bank
		// |         |__ Default Account
		// |__ Fixed Assets
		//      |__ House
		//      |__ Other Asset
		//      |__ Vehicle

		CashAccount assetRootAccount = rootAccountsMap.get(CashAccountType.ASSET.name());
		CashAccount currentAssets = new CashAccountBuilder().name("Current Assets")
				.parentAccount(assetRootAccount)
				.build();
		CashAccount defaultBank = new CashAccountBuilder().name("Default Bank")
				.parentAccount(currentAssets)
				.build();
		CashAccount defaultAccount = new CashAccountBuilder().name("Default Account")
				.parentAccount(defaultBank)
				.build();
		CashAccount fixedAssets = new CashAccountBuilder().name("Fixed Assets")
				.parentAccount(assetRootAccount)
				.build();
		CashAccount house = new CashAccountBuilder().name("House")
				.parentAccount(fixedAssets)
				.build();
		CashAccount otherAsset = new CashAccountBuilder().name("Other Asset")
				.parentAccount(fixedAssets)
				.build();

		// EQUITY
		// |__ Opening Balances

		CashAccount equityRootAccount = rootAccountsMap.get(CashAccountType.EQUITY.name());
		CashAccount openingBalances = new CashAccountBuilder().name("Opening Balances")
				.parentAccount(equityRootAccount)
				.build();

		// EXPENSE
		// |__ Adjustement
		// |__ Auto
		// |    |__ Fees
		// |    |__ Gas
		// |    |__ Parking
		// |    |__ Repair and Maintenance
		// |__ Bank Service Charge
		// |__ Beauty Expense
		// |    |__ Clothes
		// |    |__ Hair dresser
		// |__ Charity
		// |__ Computer
		// |__ Communication
		// |    |__ Cable
		// |    |__ Internet
		// |    |__ Phone
		// |__ Education
		// |__ Hobbies
		// |    |__ Bar
		// |    |__ Books
		// |    |__ Dining
		// |    |__ Movies
		// |    |__ Music
		// |    |__ Game
		// |    |__ Sport
		// |    |__ Travel
		// |__ Gifts
		// |__ Groceries
		// |__ Home
		// |    |__ Furniture
		// |    |__ Home Repair
		// |    |__ Rent
		// |__ Insurance
		// |    |__ Auto Insurance
		// |    |__ Health Insurance
		// |    |__ Home Insurance
		// |    |__ Life Insurance
		// |__ Interest
		// |    |__ Mortgage Interest
		// |__ Medical Expenses
		// |__ Miscellaneous
		// |__ Taxes
		// |    |__ Housing Tax
		// |    |__ Income taxes
		// |    |__ Other Tax
		// |    |__ Property Tax
		// |    |__ State/Province
		// |__ Transportation
		// |    |__ Car Renting
		// |    |__ Carpool
		// |    |__ Plane
		// |    |__ Public Transportation
		// |    |__ Train
		// |__ Utilities
		// |    |__ Electric
		// |    |__ Garbage collection
		// |    |__ Gas Home
		// |    |__ Water
		CashAccount expenseRootAccount = rootAccountsMap.get(CashAccountType.EXPENSE.name());
		CashAccount adjustement = new CashAccountBuilder().name("Adjustement")
				.parentAccount(expenseRootAccount)
				.build();
		CashAccount auto = new CashAccountBuilder().name("Auto")
				.parentAccount(expenseRootAccount)
				.build();
		CashAccount fees = new CashAccountBuilder().name("Fees")
				.parentAccount(auto)
				.build();
		CashAccount gas = new CashAccountBuilder().name("Gas")
				.parentAccount(auto)
				.build();
		CashAccount parking = new CashAccountBuilder().name("Parking")
				.parentAccount(auto)
				.build();
		CashAccount repairAndMaintenance = new CashAccountBuilder().name("Repair and Maintenance")
				.parentAccount(auto)
				.build();
		CashAccount bankServiceCharge = new CashAccountBuilder().name("Bank Service Charge")
				.parentAccount(expenseRootAccount)
				.build();
		CashAccount beautyExpense = new CashAccountBuilder().name("Beauty Expense")
				.parentAccount(expenseRootAccount)
				.build();
		CashAccount clothes = new CashAccountBuilder().name("Clothes")
				.parentAccount(beautyExpense)
				.build();
		CashAccount hairDresser = new CashAccountBuilder().name("Hair dresser")
				.parentAccount(beautyExpense)
				.build();
		CashAccount charity = new CashAccountBuilder().name("Charity")
				.parentAccount(expenseRootAccount)
				.build();
		CashAccount communication = new CashAccountBuilder().name("Communication")
				.parentAccount(expenseRootAccount)
				.build();
		CashAccount cable = new CashAccountBuilder().name("Cable")
				.parentAccount(communication)
				.build();
		CashAccount internet = new CashAccountBuilder().name("Internet")
				.parentAccount(communication)
				.build();
		CashAccount phone = new CashAccountBuilder().name("Phone")
				.parentAccount(communication)
				.build();
		CashAccount computer = new CashAccountBuilder().name("Computer")
				.parentAccount(expenseRootAccount)
				.build();
		CashAccount education = new CashAccountBuilder().name("Education")
				.parentAccount(expenseRootAccount)
				.build();
		CashAccount hobbies = new CashAccountBuilder().name("Hobbies")
				.parentAccount(expenseRootAccount)
				.build();
		CashAccount bar = new CashAccountBuilder().name("Bar")
				.parentAccount(hobbies)
				.build();
		CashAccount books = new CashAccountBuilder().name("Books")
				.parentAccount(hobbies)
				.build();
		CashAccount dining = new CashAccountBuilder().name("Dining")
				.parentAccount(hobbies)
				.build();
		CashAccount movies = new CashAccountBuilder().name("Movies")
				.parentAccount(hobbies)
				.build();
		CashAccount music = new CashAccountBuilder().name("Music")
				.parentAccount(hobbies)
				.build();
		CashAccount game = new CashAccountBuilder().name("Game")
				.parentAccount(hobbies)
				.build();
		CashAccount sport = new CashAccountBuilder().name("Sport")
				.parentAccount(hobbies)
				.build();
		CashAccount travel = new CashAccountBuilder().name("Travel")
				.parentAccount(hobbies)
				.build();
		CashAccount gifts = new CashAccountBuilder().name("Gifts")
				.parentAccount(expenseRootAccount)
				.build();
		CashAccount groceries = new CashAccountBuilder().name("Groceries")
				.parentAccount(expenseRootAccount)
				.build();
		CashAccount home = new CashAccountBuilder().name("Home")
				.parentAccount(expenseRootAccount)
				.build();
		CashAccount furniture = new CashAccountBuilder().name("Furniture")
				.parentAccount(home)
				.build();
		CashAccount homeRepair = new CashAccountBuilder().name("Home Repair")
				.parentAccount(home)
				.build();
		CashAccount rent = new CashAccountBuilder().name("Rent")
				.parentAccount(home)
				.build();
		CashAccount insurance = new CashAccountBuilder().name("Insurance")
				.parentAccount(expenseRootAccount)
				.build();
		CashAccount autoInsurance = new CashAccountBuilder().name("Auto Insurance")
				.parentAccount(insurance)
				.build();
		CashAccount healthInsurance = new CashAccountBuilder().name("Health Insurance")
				.parentAccount(insurance)
				.build();
		CashAccount homeInsurance = new CashAccountBuilder().name("Home Insurance")
				.parentAccount(insurance)
				.build();
		CashAccount lifeInsurance = new CashAccountBuilder().name("Life Insurance")
				.parentAccount(insurance)
				.build();
		CashAccount interest = new CashAccountBuilder().name("Interest")
				.parentAccount(expenseRootAccount)
				.build();
		CashAccount mortgageInterest = new CashAccountBuilder().name("Mortgage Interest")
				.parentAccount(interest)
				.build();
		CashAccount medicalExpenses = new CashAccountBuilder().name("Medical Expenses")
				.parentAccount(expenseRootAccount)
				.build();
		CashAccount miscellaneous = new CashAccountBuilder().name("Miscellaneous")
				.parentAccount(expenseRootAccount)
				.build();
		CashAccount taxes = new CashAccountBuilder().name("Taxes")
				.parentAccount(expenseRootAccount)
				.build();
		CashAccount housingTax = new CashAccountBuilder().name("Housing Tax")
				.parentAccount(taxes)
				.build();
		CashAccount incomeTaxes = new CashAccountBuilder().name("Income taxes")
				.parentAccount(taxes)
				.build();
		CashAccount otherTax = new CashAccountBuilder().name("Other Tax")
				.parentAccount(taxes)
				.build();
		CashAccount propertyTax = new CashAccountBuilder().name("Property Tax")
				.parentAccount(taxes)
				.build();
		CashAccount stateProvinceTax = new CashAccountBuilder().name("State Province Tax")
				.parentAccount(taxes)
				.build();
		CashAccount transportation = new CashAccountBuilder().name("Transportation")
				.parentAccount(expenseRootAccount)
				.build();
		CashAccount carRenting = new CashAccountBuilder().name("Car Renting")
				.parentAccount(transportation)
				.build();
		CashAccount carpool = new CashAccountBuilder().name("Carpool")
				.parentAccount(transportation)
				.build();
		CashAccount plane = new CashAccountBuilder().name("Plane")
				.parentAccount(transportation)
				.build();
		CashAccount publicTransportation = new CashAccountBuilder().name("Public Transportation")
				.parentAccount(transportation)
				.build();
		CashAccount train = new CashAccountBuilder().name("Train")
				.parentAccount(transportation)
				.build();
		CashAccount utilities = new CashAccountBuilder().name("Utilities")
				.parentAccount(expenseRootAccount)
				.build();
		CashAccount electric = new CashAccountBuilder().name("Electric")
				.parentAccount(utilities)
				.build();
		CashAccount garbageCollection = new CashAccountBuilder().name("Garbage collection")
				.parentAccount(utilities)
				.build();
		CashAccount gasHome = new CashAccountBuilder().name("Gas Home")
				.parentAccount(utilities)
				.build();
		CashAccount water = new CashAccountBuilder().name("Water")
				.parentAccount(utilities)
				.build();

		// INCOME
		// |__ Bonus
		// |__ Gifts Received
		// |__ Interest Income
		// |    |__ Checking Interest
		// |    |__ Other Interest
		// |    |__ Saving Interest
		// |__ Other Income
		// |__ Salary
		// |__ Welfare Benefit
		CashAccount incomeRootAccount = rootAccountsMap.get(CashAccountType.INCOME.name());
		CashAccount bonus = new CashAccountBuilder().name("Bonus")
				.parentAccount(incomeRootAccount)
				.build();
		CashAccount giftsReceived = new CashAccountBuilder().name("Gifts Received")
				.parentAccount(incomeRootAccount)
				.build();
		CashAccount interestIncome = new CashAccountBuilder().name("Interest Income")
				.parentAccount(incomeRootAccount)
				.build();
		CashAccount checkingInterest = new CashAccountBuilder().name("Checking Interest")
				.parentAccount(interestIncome)
				.build();
		CashAccount otherInterest = new CashAccountBuilder().name("Other Interest")
				.parentAccount(interestIncome)
				.build();
		CashAccount savingInterest = new CashAccountBuilder().name("Saving Interest")
				.parentAccount(interestIncome)
				.build();
		CashAccount otherIncome = new CashAccountBuilder().name("Other Income")
				.parentAccount(incomeRootAccount)
				.build();
		CashAccount salary = new CashAccountBuilder().name("Salary")
				.parentAccount(incomeRootAccount)
				.build();
		CashAccount welfareBenefit = new CashAccountBuilder().name("Welfare Benefit")
				.parentAccount(incomeRootAccount)
				.build();

		// LIABILITY
		// |__ Credit Card
		// |__ Mortgage Loan
		CashAccount liabilityAccount = rootAccountsMap.get(CashAccountType.LIABILITY.name());
		CashAccount creditCard = new CashAccountBuilder().name("Credit Card")
				.parentAccount(liabilityAccount)
				.build();
		CashAccount mortgageLoan = new CashAccountBuilder().name("Mortgage Loan")
				.parentAccount(liabilityAccount)
				.build();

		return cashAccountList;
	}
}
