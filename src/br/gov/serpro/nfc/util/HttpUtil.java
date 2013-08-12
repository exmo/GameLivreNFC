package br.gov.serpro.nfc.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Utilitário para conexão com a internet.
 * 
 * @author Marlon Silva Carvalho
 */
final public class HttpUtil {
	private static DefaultHttpClient client;

	private HttpUtil() {
	}

	/**
	 * Conectar com um servidor e obter a resposta através de GET.
	 * 
	 * @param url
	 *            URL do Servidor.
	 * @return Resposta.
	 */
	synchronized public static String performGet(final String url) {
		try {
			URI uri = new URI(url);
			HttpGet get = new HttpGet(uri);

			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(final HttpResponse response) throws HttpResponseException, IOException {
					String result = null;

					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() >= 300) {
						throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
					}

					HttpEntity entity = response.getEntity();
					if (entity != null) {
						result = EntityUtils.toString(entity, "UTF-8");
						entity.consumeContent();
					}

					return result;
				}
			};

			return getClient().execute(get, responseHandler);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public static Bitmap getBitmap(String strURL) {
		Bitmap result = null;
		InputStream inputStream = null;

		try {
			URL url = new URL(strURL);
			URLConnection conn = url.openConnection();

			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				inputStream = httpConn.getInputStream();
			}

			BitmapFactory.Options bmOptions;
			bmOptions = new BitmapFactory.Options();
			bmOptions.inSampleSize = 1;

			result = BitmapFactory.decodeStream(inputStream, null, bmOptions);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	public static HttpClient getClient() {
		if (client == null) {
			int timeoutConnection = 10000;
			int timeoutSocket = 10000;

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpConnectionParams.setConnectionTimeout(params, timeoutConnection);
			HttpConnectionParams.setSoTimeout(params, timeoutSocket);
			HttpProtocolParams.setContentCharset(params, "utf-8");

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

			ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
			client = new DefaultHttpClient(manager, params);
		}

		return client;
	}

}
