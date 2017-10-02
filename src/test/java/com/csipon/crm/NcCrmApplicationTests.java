package com.csipon.crm;


import com.csipon.crm.datagenerator.GeneratorDbData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NcCrmApplicationTests {

	@Autowired
	private GeneratorDbData generator;

	@Test
	public void contextLoads() throws SQLException {
	}

	@Test
	public void generateTestDataForDB() throws SQLException {
		generator.generateDataForDB(1);
	}
}
