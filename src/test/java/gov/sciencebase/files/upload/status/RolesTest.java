package gov.sciencebase.files.upload.status;

import org.junit.Test;
import org.xml.sax.SAXParseException;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RolesTest {
    private static final String authuser = "betasciencebaseoauthtestservice";
    private static final char[] authpassword = new char[]{'7', 'N', 'C', '<', 'x', 'P', 'B', 'z', 'Y', '\\', '@', 'U'};
    private static final String testuser = "djwilliams@usgs.gov";

    private static final String[] roles = new String[]{
            "2010WQMeeting_Readers",   "CDI_Readers",                    "ClimateChange_Authors", "CSS_Readers",
            "usgsegis_Readers",        "GeologyScienceStrategy_Authors", "GHG_Readers",           "GMIS",
            "JDT_Authors",             "JDT_FORT_Developers",            "myUSGS-Help_Readers",   "MyUSGS",
            "TrustedApps",             "USGS",                           "ScienceBase_DataAdmin", "ScienceBase_Managers",
            "ScienceBase_SystemAdmin", "Water-Tech-Reviews_Readers"};

    @Test
    public void getRoles() throws Exception {
        final List<String> expected = Arrays.asList(roles);
        final Collection<String> actual = Roles.getRoles(authuser, CharBuffer.wrap(authpassword), testuser);
        assertEquals("This should be a pretty big list", expected, actual);
    }

    @Test(expected=SAXParseException.class)
    public void getStringsBadString() throws Exception {
        final String input = "abc";//any bad string should do here
        Roles.getGroupNames(input);
    }

    @Test
    public void getStringsGoldenPath() throws Exception {
        final List<String> expected = Arrays.asList(roles);
        final String groupXmlChunk = expected.stream().reduce("", (x, y) -> x + "<group name=\"" + y +"\"/>");
        final String input = "<user id='222'>" +
                                 "<name>" + testuser + "</name>" +
                                 "<groups>" +
                                     groupXmlChunk +
                                 "</groups>" +
                             "</user>";

        final List<String> actual = Roles.getGroupNames(input);
        assertEquals("This should be a pretty big list", expected, actual);
    }

    @Test
    public void getStringsEmpty() throws Exception {
        final String input = "<user><groups></groups></user>";
        final List<String> actual = Roles.getGroupNames(input);
        assertEquals("This should be empty list", Collections.EMPTY_LIST, actual);
    }

}