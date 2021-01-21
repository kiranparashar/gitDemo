package com.yatra.utils;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

	@Override
	public void onTestStart(ITestResult result) {
		System.out.println("Started Test: " + result.getName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		System.out.println("Finished Test: " + result.getName() + " :PASSED");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		System.out.println("Finished Test: " + result.getName() + " :FAILED");
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		System.out.println("Finished Test: " + result.getName() + " :SKIPPED");
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		System.out.println("Finished Test: " + result.getName()
				+ " :FAILED BUT WITHIN SUCCESS PERCENTAGE");
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		
	}
}
