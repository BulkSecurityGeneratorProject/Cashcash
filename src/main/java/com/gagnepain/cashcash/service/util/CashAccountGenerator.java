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
		cashAccountList.add(currentAssets);
		CashAccount defaultBank = new CashAccountBuilder().name("Default Bank")
				.parentAccount(currentAssets)
				.build();
		cashAccountList.add(defaultBank);
		CashAccount defaultAccount = new CashAccountBuilder().name("Default Account")
				.parentAccount(defaultBank)
				.build();
		cashAccountList.add(defaultAccount);
		CashAccount fixedAssets = new CashAccountBuilder().name("Fixed Assets")
				.parentAccount(assetRootAccount)
				.build();
		cashAccountList.add(fixedAssets);
		CashAccount house = new CashAccountBuilder().name("House")
				.parentAccount(fixedAssets)
				.build();
		cashAccountList.add(house);
		CashAccount otherAsset = new CashAccountBuilder().name("Other Asset")
				.parentAccount(fixedAssets)
				.build();
		cashAccountList.add(otherAsset);

		// EQUITY
		// |__ Opening Balances

		CashAccount equityRootAccount = rootAccountsMap.get(CashAccountType.EQUITY.name());
		CashAccount openingBalances = new CashAccountBuilder().name("Opening Balances")
				.parentAccount(equityRootAccount)
				.build();
		cashAccountList.add(openingBalances);

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
		cashAccountList.add(adjustement);
		CashAccount auto = new CashAccountBuilder().name("Auto")
				.parentAccount(expenseRootAccount)
				.build();
		cashAccountList.add(auto);
		CashAccount fees = new CashAccountBuilder().name("Fees")
				.parentAccount(auto)
				.build();
		cashAccountList.add(fees);
		CashAccount gas = new CashAccountBuilder().name("Gas")
				.parentAccount(auto)
				.build();
		cashAccountList.add(gas);
		CashAccount parking = new CashAccountBuilder().name("Parking")
				.parentAccount(auto)
				.build();
		cashAccountList.add(parking);
		CashAccount repairAndMaintenance = new CashAccountBuilder().name("Repair and Maintenance")
				.parentAccount(auto)
				.build();
		cashAccountList.add(repairAndMaintenance);
		CashAccount bankServiceCharge = new CashAccountBuilder().name("Bank Service Charge")
				.parentAccount(expenseRootAccount)
				.build();
		cashAccountList.add(bankServiceCharge);
		CashAccount beautyExpense = new CashAccountBuilder().name("Beauty Expense")
				.parentAccount(expenseRootAccount)
				.build();
		cashAccountList.add(beautyExpense);
		CashAccount clothes = new CashAccountBuilder().name("Clothes")
				.parentAccount(beautyExpense)
				.build();
		cashAccountList.add(clothes);
		CashAccount hairDresser = new CashAccountBuilder().name("Hair dresser")
				.parentAccount(beautyExpense)
				.build();
		cashAccountList.add(hairDresser);
		CashAccount charity = new CashAccountBuilder().name("Charity")
				.parentAccount(expenseRootAccount)
				.build();
		cashAccountList.add(charity);
		CashAccount communication = new CashAccountBuilder().name("Communication")
				.parentAccount(expenseRootAccount)
				.build();
		cashAccountList.add(communication);
		CashAccount cable = new CashAccountBuilder().name("Cable")
				.parentAccount(communication)
				.build();
		cashAccountList.add(cable);
		CashAccount internet = new CashAccountBuilder().name("Internet")
				.parentAccount(communication)
				.build();
		cashAccountList.add(internet);
		CashAccount phone = new CashAccountBuilder().name("Phone")
				.parentAccount(communication)
				.build();
		cashAccountList.add(phone);
		CashAccount computer = new CashAccountBuilder().name("Computer")
				.parentAccount(expenseRootAccount)
				.build();
		cashAccountList.add(computer);
		CashAccount education = new CashAccountBuilder().name("Education")
				.parentAccount(expenseRootAccount)
				.build();
		cashAccountList.add(education);
		CashAccount hobbies = new CashAccountBuilder().name("Hobbies")
				.parentAccount(expenseRootAccount)
				.build();
		cashAccountList.add(hobbies);
		CashAccount bar = new CashAccountBuilder().name("Bar")
				.parentAccount(hobbies)
				.build();
		cashAccountList.add(bar);
		CashAccount books = new CashAccountBuilder().name("Books")
				.parentAccount(hobbies)
				.build();
		cashAccountList.add(books);
		CashAccount dining = new CashAccountBuilder().name("Dining")
				.parentAccount(hobbies)
				.build();
		cashAccountList.add(dining);
		CashAccount movies = new CashAccountBuilder().name("Movies")
				.parentAccount(hobbies)
				.build();
		cashAccountList.add(movies);
		CashAccount music = new CashAccountBuilder().name("Music")
				.parentAccount(hobbies)
				.build();
		cashAccountList.add(music);
		CashAccount game = new CashAccountBuilder().name("Game")
				.parentAccount(hobbies)
				.build();
		cashAccountList.add(game);
		CashAccount sport = new CashAccountBuilder().name("Sport")
				.parentAccount(hobbies)
				.build();
		cashAccountList.add(sport);
		CashAccount travel = new CashAccountBuilder().name("Travel")
				.parentAccount(hobbies)
				.build();
		cashAccountList.add(travel);
		CashAccount gifts = new CashAccountBuilder().name("Gifts")
				.parentAccount(expenseRootAccount)
				.build();
		cashAccountList.add(gifts);
		CashAccount groceries = new CashAccountBuilder().name("Groceries")
				.parentAccount(expenseRootAccount)
				.build();
		cashAccountList.add(groceries);
		CashAccount home = new CashAccountBuilder().name("Home")
				.parentAccount(expenseRootAccount)
				.build();
		cashAccountList.add(home);
		CashAccount furniture = new CashAccountBuilder().name("Furniture")
				.parentAccount(home)
				.build();
		cashAccountList.add(furniture);
		CashAccount homeRepair = new CashAccountBuilder().name("Home Repair")
				.parentAccount(home)
				.build();
		cashAccountList.add(homeRepair);
		CashAccount rent = new CashAccountBuilder().name("Rent")
				.parentAccount(home)
				.build();
		cashAccountList.add(rent);
		CashAccount insurance = new CashAccountBuilder().name("Insurance")
				.parentAccount(expenseRootAccount)
				.build();
		cashAccountList.add(insurance);
		CashAccount autoInsurance = new CashAccountBuilder().name("Auto Insurance")
				.parentAccount(insurance)
				.build();
		cashAccountList.add(autoInsurance);
		CashAccount healthInsurance = new CashAccountBuilder().name("Health Insurance")
				.parentAccount(insurance)
				.build();
		cashAccountList.add(healthInsurance);
		CashAccount homeInsurance = new CashAccountBuilder().name("Home Insurance")
				.parentAccount(insurance)
				.build();
		cashAccountList.add(homeInsurance);
		CashAccount lifeInsurance = new CashAccountBuilder().name("Life Insurance")
				.parentAccount(insurance)
				.build();
		cashAccountList.add(lifeInsurance);
		CashAccount interest = new CashAccountBuilder().name("Interest")
				.parentAccount(expenseRootAccount)
				.build();
		cashAccountList.add(interest);
		CashAccount mortgageInterest = new CashAccountBuilder().name("Mortgage Interest")
				.parentAccount(interest)
				.build();
		cashAccountList.add(mortgageInterest);
		CashAccount medicalExpenses = new CashAccountBuilder().name("Medical Expenses")
				.parentAccount(expenseRootAccount)
				.build();
		cashAccountList.add(medicalExpenses);
		CashAccount miscellaneous = new CashAccountBuilder().name("Miscellaneous")
				.parentAccount(expenseRootAccount)
				.build();
		cashAccountList.add(miscellaneous);
		CashAccount taxes = new CashAccountBuilder().name("Taxes")
				.parentAccount(expenseRootAccount)
				.build();
		cashAccountList.add(taxes);
		CashAccount housingTax = new CashAccountBuilder().name("Housing Tax")
				.parentAccount(taxes)
				.build();
		cashAccountList.add(housingTax);
		CashAccount incomeTaxes = new CashAccountBuilder().name("Income taxes")
				.parentAccount(taxes)
				.build();
		cashAccountList.add(incomeTaxes);
		CashAccount otherTax = new CashAccountBuilder().name("Other Tax")
				.parentAccount(taxes)
				.build();
		cashAccountList.add(otherTax);
		CashAccount propertyTax = new CashAccountBuilder().name("Property Tax")
				.parentAccount(taxes)
				.build();
		cashAccountList.add(propertyTax);
		CashAccount stateProvinceTax = new CashAccountBuilder().name("State Province Tax")
				.parentAccount(taxes)
				.build();
		cashAccountList.add(stateProvinceTax);
		CashAccount transportation = new CashAccountBuilder().name("Transportation")
				.parentAccount(expenseRootAccount)
				.build();
		cashAccountList.add(transportation);
		CashAccount carRenting = new CashAccountBuilder().name("Car Renting")
				.parentAccount(transportation)
				.build();
		cashAccountList.add(carRenting);
		CashAccount carpool = new CashAccountBuilder().name("Carpool")
				.parentAccount(transportation)
				.build();
		cashAccountList.add(carpool);
		CashAccount plane = new CashAccountBuilder().name("Plane")
				.parentAccount(transportation)
				.build();
		cashAccountList.add(plane);
		CashAccount publicTransportation = new CashAccountBuilder().name("Public Transportation")
				.parentAccount(transportation)
				.build();
		cashAccountList.add(publicTransportation);
		CashAccount train = new CashAccountBuilder().name("Train")
				.parentAccount(transportation)
				.build();
		cashAccountList.add(train);
		CashAccount utilities = new CashAccountBuilder().name("Utilities")
				.parentAccount(expenseRootAccount)
				.build();
		cashAccountList.add(utilities);
		CashAccount electric = new CashAccountBuilder().name("Electric")
				.parentAccount(utilities)
				.build();
		cashAccountList.add(electric);
		CashAccount garbageCollection = new CashAccountBuilder().name("Garbage collection")
				.parentAccount(utilities)
				.build();
		cashAccountList.add(garbageCollection);
		CashAccount gasHome = new CashAccountBuilder().name("Gas Home")
				.parentAccount(utilities)
				.build();
		cashAccountList.add(gasHome);
		CashAccount water = new CashAccountBuilder().name("Water")
				.parentAccount(utilities)
				.build();
		cashAccountList.add(water);

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
		cashAccountList.add(bonus);
		CashAccount giftsReceived = new CashAccountBuilder().name("Gifts Received")
				.parentAccount(incomeRootAccount)
				.build();
		cashAccountList.add(giftsReceived);
		CashAccount interestIncome = new CashAccountBuilder().name("Interest Income")
				.parentAccount(incomeRootAccount)
				.build();
		cashAccountList.add(interestIncome);
		CashAccount checkingInterest = new CashAccountBuilder().name("Checking Interest")
				.parentAccount(interestIncome)
				.build();
		cashAccountList.add(checkingInterest);
		CashAccount otherInterest = new CashAccountBuilder().name("Other Interest")
				.parentAccount(interestIncome)
				.build();
		cashAccountList.add(otherInterest);
		CashAccount savingInterest = new CashAccountBuilder().name("Saving Interest")
				.parentAccount(interestIncome)
				.build();
		cashAccountList.add(savingInterest);
		CashAccount otherIncome = new CashAccountBuilder().name("Other Income")
				.parentAccount(incomeRootAccount)
				.build();
		cashAccountList.add(otherIncome);
		CashAccount salary = new CashAccountBuilder().name("Salary")
				.parentAccount(incomeRootAccount)
				.build();
		cashAccountList.add(salary);
		CashAccount welfareBenefit = new CashAccountBuilder().name("Welfare Benefit")
				.parentAccount(incomeRootAccount)
				.build();
		cashAccountList.add(welfareBenefit);

		// LIABILITY
		// |__ Credit Card
		// |__ Mortgage Loan
		CashAccount liabilityAccount = rootAccountsMap.get(CashAccountType.LIABILITY.name());
		CashAccount creditCard = new CashAccountBuilder().name("Credit Card")
				.parentAccount(liabilityAccount)
				.build();
		cashAccountList.add(creditCard);
		CashAccount mortgageLoan = new CashAccountBuilder().name("Mortgage Loan")
				.parentAccount(liabilityAccount)
				.build();
		cashAccountList.add(mortgageLoan);

		return cashAccountList;
	}
}
