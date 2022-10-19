import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    private static final String IN_FILE_TXT = "src\\inFile.txt";
    private static final String OUT_FILE_TXT = "src\\outFile.txt";
    private static final String PATH_TO_PICTURE = "src\\Pictures\\picture";

    public static void main(String[] args) {
        String Url;

        try (BufferedReader inFile = new BufferedReader(new FileReader(IN_FILE_TXT));
             BufferedWriter outFile = new BufferedWriter(new FileWriter(OUT_FILE_TXT))) {
            while ((Url = inFile.readLine()) != null) {
                URL url = new URL(Url);

                //String result1;
                String result;
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    //result1 = bufferedReader.lines().collect(Collectors.joining("\n"));
                    result = bufferedReader.lines().collect(Collectors.joining("\n"));
                }
                //Pattern email_pattern1 = Pattern.compile("//akspic.ru/image(.*)\"");
                //Matcher matcher1 = email_pattern1.matcher(result1);
                Pattern email_pattern = Pattern.compile("//img2.akspic.ru/previews/(.+?).jpg");
                Matcher matcher = email_pattern.matcher(result);
                int i = 0;
                //if(matcher1.find()) {
                    while (matcher.find() && i < 5) {
                        outFile.write(matcher.group() + "\n");
                        i++;
                    }
               // }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader musicFile = new BufferedReader(new FileReader(OUT_FILE_TXT))) {
            String picture;
            int count = 0;
            try {
                while ((picture = musicFile.readLine()) != null) {
                    downloadUsingNIO("https:" + picture, PATH_TO_PICTURE + String.valueOf(count) + ".jpg");
                    count++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void downloadUsingNIO(String strUrl, String file) throws IOException {
        URL url = new URL(strUrl);
        ReadableByteChannel byteChannel = Channels.newChannel(url.openStream());
        FileOutputStream stream = new FileOutputStream(file);
        stream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
        stream.close();
        byteChannel.close();
    }
}