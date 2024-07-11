public class TextParser {
    public Command parse(String input) {
        String[] words = input.split(" ");
        if (words.length == 0) {
            return null;
        }
        String commandWord = words[0];
        String secondWord = words.length > 1 ? words[1] : null;
        return new Command(commandWord, secondWord);
    }
}
