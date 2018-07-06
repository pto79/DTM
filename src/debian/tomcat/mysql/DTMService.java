package debian.tomcat.mysql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("")
public class DTMService {
	
	String url = "https://api.iextrading.com/1.0/stock/aapl/price";
	DTMDao dtmDao = null;
	ResultSet rs = null;
	
	public DTMService() {
		dtmDao = new DTMDao();
	}
	
	@GET
	public String testGet()
	{
		Date d = new Date();
		return d.toString();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response testPost(testData td)
	{
		System.out.println(td.name + td.value);
		return Response.status(Status.OK).entity(td).build();
	}
	
	@GET
	@Path("/HttpGet")
	public String testHttpGet()
	{	
		//https://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
		
		try {
			URL obj = new URL(url);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	
			// optional default is GET
			con.setRequestMethod("GET");
	
			//add request header
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
	
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
	
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			//print result
			return response.toString();			
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Date d = new Date();
		return d.toString(); 
	}
	
	@GET
	@Path("JerseyGet")
	public String testJerseyGet()
	{
		
		Response response = ClientBuilder.newClient().target(url).request(MediaType.APPLICATION_JSON).get();		
		
		//Client client = ClientBuilder.newClient();
		//WebTarget webTarget = client.target(url).path("resource").queryParam("name", "value");		
		//Invocation.Builder invoBld = webTarget.request(MediaType.APPLICATION_JSON);
		//invoBld.header("name", "value");
		//String responseMsg = webTarget.request().get(String.class);
		//Response response = invoBld.get();
		
		//https://jersey.github.io/documentation/latest/client.html#d0e4859
//		Client client = ClientBuilder.newClient(new ClientConfig()
//	            .register(MyClientResponseFilter.class)
//	            .register(new AnotherClientFilter()));
//	 
//		String entity = client.target("http://example.com/rest")
//	            .register(FilterForExampleCom.class)
//	            .path("resource/helloworld")
//	            .queryParam("greeting", "Hi World!")
//	            .request(MediaType.TEXT_PLAIN_TYPE)
//	            .header("some-header", "true")
//	            .get(String.class);		

		return response.readEntity(String.class);
	}
	
	@GET
	@Path("JerseyPost")
	public testData testJerseyPost()
	{
		testData td = new testData();
		td.name = "name";
		td.value = "value";
		Response response = ClientBuilder.newClient().target("http://172.16.23.88:8080/DTM").path("service").request(MediaType.APPLICATION_JSON).post(Entity.entity(td, MediaType.APPLICATION_JSON));
		return response.readEntity(testData.class);
	}
	
	@POST
	@Path("testDao")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response testDaoCreate(testData td)
	{
		dtmDao.testCreate(td);
		return testDaoRetrieve();
	}
	
	@PUT
	@Path("testDao")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response testDaoUpdate(testData td)
	{
		dtmDao.testUpdate(td);
		return testDaoRetrieve();
	}
	
	@DELETE
	@Path("testDao")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response testDaoDelete(testData td)
	{
		dtmDao.testDelete(td);
		return testDaoRetrieve();
	}
	
	@GET
	@Path("testDao")
	@Produces(MediaType.APPLICATION_JSON)
	public Response testDaoRetrieve()
	{
		ArrayList<testData> tdArray = new ArrayList<testData>();
		rs = dtmDao.testRetrieve();
		try {
			while(rs.next())
			{
				testData tdData = new testData();
				tdData.id = rs.getInt("id");
				tdData.name = rs.getString("name");
				tdData.value = rs.getString("value");
				tdArray.add(tdData);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(Status.OK).entity(tdArray).build();
	}
	
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(
		@FormDataParam("file") InputStream uploadedInputStream,
		@FormDataParam("file") FormDataContentDisposition fileDetail) {

		String uploadedFileLocation = "d://uploaded/" + fileDetail.getFileName();

		// save it
		DTMUtil.writeToFile(uploadedInputStream, uploadedFileLocation);

		String output = "File uploaded to : " + uploadedFileLocation;

		return Response.status(200).entity(output).build();

	}
}
