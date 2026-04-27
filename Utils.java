public class Utils {
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
