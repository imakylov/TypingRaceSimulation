/**
 * A class that holds static methods and constants useful for different purposes.
 *
 * @author Adil Akylov
 * @version 1.1
 */
public class Utils {
    /**
     * takes some text and breakes it into lines between words to be at max limit width.
     * A line can only be more if there is a word that is longer then limit
     *
     * @param passage the text that needs to be broken
     * @param limit max width of a line
     * @return the passage with some spaces replace with new lines to be at max limit width
     */
    public static String breakWords(String passage, int limit){
        String result = "";
        String[] words = passage.split(" ");
        if(words.length > 0) result = words[0];
        if(words.length < 2) return result;
        int curCol = words[0].length();
        for(int i=1;i<words.length;i++){
            if(curCol + words[i].length() > limit){
                result += "\n";
                curCol = 0;
            }else{
                result += " ";
            }
            curCol += words[i].length();
            result += words[i];
        }
        return result;
    }
}
