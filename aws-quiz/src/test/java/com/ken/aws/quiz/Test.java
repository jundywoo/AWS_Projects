package com.ken.aws.quiz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

public class Test {

	public static void main(final String[] args) throws IOException {
		final HttpClient httpClient = HttpClientBuilder.create().build();

		final HttpUriRequest postRequestLogin = new HttpPost("http://localhost:8080/login");

		final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("username", "user"));
		urlParameters.add(new BasicNameValuePair("password", "password"));
		((HttpPost) postRequestLogin).setEntity(new UrlEncodedFormEntity(urlParameters));

		final HttpResponse responseLogin = httpClient.execute(postRequestLogin);
		responseLogin.getParams();
	}

}
