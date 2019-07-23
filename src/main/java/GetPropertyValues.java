import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class GetPropertyValues {
    String result = "";
    InputStream inputStream;

    public String getProperyValues() throws IOException {
        try {
            Properties properties = new Properties();
            String propertiesFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file ' " + propertiesFileName + " ' not found");
            }

             
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return result;
    }
}
