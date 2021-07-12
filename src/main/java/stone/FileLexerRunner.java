package stone;
import stone.stone.CodeDialog;
import stone.stone.Lexer;
import stone.stone.ParseException;
import stone.stone.Token;

import java.io.FileNotFoundException;

public class FileLexerRunner {      //分割 选中文件的 “单词”
    public static void main(String[] args) throws ParseException {
        try {
            Lexer l = new Lexer(CodeDialog.file());
            for (Token t; (t = l.read()) != Token.EOF; )
                System.out.println("=> " + t.getText());
        }
        catch(FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
