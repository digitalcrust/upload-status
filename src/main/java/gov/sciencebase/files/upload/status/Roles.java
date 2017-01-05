package gov.sciencebase.files.upload.status;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class Roles {
    /**
     * From a string representation of XML find all elements named 'group' and get the value of the 'name' attribute
     * @param result
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static List<String> getGroupNames(String result) throws ParserConfigurationException, IOException, SAXException {
        final List<String> resultNames = new ArrayList<>();
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);//TODO is this really needed
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final InputSource is = new InputSource(new StringReader(result));
        final Document doc = builder.parse(is);
        final NodeList nl = doc.getElementsByTagName("group");

        for (int i = 0; i < nl.getLength(); i++) {
            resultNames.add((nl.item(i).getAttributes().getNamedItem("name").getNodeValue()));
        }

        return resultNames;
    }
    public static Collection<String> getRoles(final String username, final CharSequence password, final String forUser)
            throws IOException {
        final HttpClient instance = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
        Collection<String> result = Collections.EMPTY_LIST;

        final String url = "https://my-beta.usgs.gov/josso/signon/usernamePasswordLogin.do" +
                "?josso_username=" + URLEncoder.encode(username, "UTF-8") +
                "&josso_password=" + URLEncoder.encode(String.valueOf(password), "UTF-8") +
                "&josso_cmd=josso";
        instance.execute(new HttpPost(url));
        final String rolesUrl = "https://beta.sciencebase.gov/directory/deprecatedLegacyUser/grabUser?username=" + forUser;
        final HttpResponse rolesResponse = instance.execute(new HttpGet(rolesUrl));
        final String contents = read(rolesResponse.getEntity().getContent());

        try {
            result = getGroupNames(contents);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();//TODO logger here at least
        } catch (SAXException e) {
            e.printStackTrace();//TODO logger here at least
        }
        return result;
    }
    public static String read(final InputStream input) throws IOException {
        try (final BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

}
