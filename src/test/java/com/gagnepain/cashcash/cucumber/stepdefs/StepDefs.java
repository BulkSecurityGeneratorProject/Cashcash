package com.gagnepain.cashcash.cucumber.stepdefs;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import com.gagnepain.cashcash.CashcashApp;

@RunWith(SpringRunner.class)
@ContextConfiguration(loader = SpringBootContextLoader.class,
		classes = CashcashApp.class)
public abstract class StepDefs {
	protected ResultActions actions;
}
