/* The following code was generated by JFlex 1.4.3 on 16/10/2018, 11:25 */

package de.dfki.vsm.model.scenescript;

// Import Directives
import de.dfki.vsm.util.syn.*;
import java.io.*;

/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.3
 * on 16/10/2018, 11:25 from the specification file
 * <tt>C:/Users/lenny/IdeaProjects/sopra2018/VisualSceneMaker/src/de/dfki/vsm/model/scenescript/lexxer.jflex</tt>
 */
public final class ScriptLexxer extends SyntaxDocLexxer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YY_SCENE_NAME = 8;
  public static final int YY_ACTION_ACTIVITY = 30;
  public static final int YY_TURN_BODY = 18;
  public static final int YY_SCENE_LANG = 6;
  public static final int YY_ACTION_BODY = 24;
  public static final int YY_TURN_HEAD = 12;
  public static final int YY_TURN_FOOT = 20;
  public static final int YY_SCENE_BODY = 10;
  public static final int YYCOMMENT = 28;
  public static final int YY_TURN_INIT = 16;
  public static final int YYINITIAL = 0;
  public static final int YY_TURN_NAME = 14;
  public static final int YY_SCENE_HEAD = 2;
  public static final int YY_SCENE_UNDL = 4;
  public static final int YY_ERROR_STATE = 26;
  public static final int YY_VARIABLE = 22;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1,  1,  2,  2,  3,  3,  4,  4,  5,  5,  6,  6,  7,  7, 
     8,  8,  9,  9, 10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 15, 15
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\16\1\20\1\0\1\16\1\17\22\0\1\16\1\13\1\33"+
    "\1\12\1\7\2\12\1\3\1\12\1\12\1\14\1\12\1\13\1\15"+
    "\1\10\1\40\1\32\11\31\1\2\1\13\1\34\1\6\1\37\1\13"+
    "\23\11\1\43\7\11\1\4\1\12\1\5\1\0\1\1\1\0\1\26"+
    "\1\11\1\41\1\11\1\24\1\25\1\36\1\35\3\11\1\27\1\11"+
    "\1\42\3\11\1\22\1\30\1\21\1\23\5\11\1\0\1\12\1\0"+
    "\1\12\66\0\1\11\12\0\3\11\1\0\1\11\1\0\6\11\2\0"+
    "\2\11\4\0\1\11\1\0\1\11\5\0\1\11\2\0\2\11\1\0"+
    "\1\11\1\0\1\11\1\0\6\11\2\0\2\11\4\0\1\11\1\0"+
    "\1\11\4\0\2\11\2\0\1\11\uff00\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\20\0\1\1\1\2\1\3\2\4\2\1\1\5\2\1"+
    "\1\6\1\7\1\10\2\11\1\12\2\13\1\14\1\1"+
    "\1\15\1\16\1\17\1\20\1\21\1\22\1\16\1\23"+
    "\1\16\2\22\1\16\1\24\1\25\1\26\1\16\2\26"+
    "\1\16\1\27\1\16\1\30\1\31\1\30\1\32\1\33"+
    "\1\34\1\35\1\30\1\36\1\15\2\35\2\37\1\30"+
    "\1\40\1\41\2\0\1\42\3\0\1\43\2\35\5\0"+
    "\2\35\1\44\1\0\1\45\2\0\1\46\1\47\50\0"+
    "\1\22\1\26";

  private static int [] zzUnpackAction() {
    int [] result = new int[139];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\44\0\110\0\154\0\220\0\264\0\330\0\374"+
    "\0\u0120\0\u0144\0\u0168\0\u018c\0\u01b0\0\u01d4\0\u01f8\0\u021c"+
    "\0\u0240\0\u0240\0\u0240\0\u0264\0\u0240\0\u0288\0\u02ac\0\u0240"+
    "\0\u02d0\0\u02f4\0\u0318\0\u0240\0\u0240\0\u033c\0\u0240\0\u0360"+
    "\0\u0384\0\u0240\0\u0240\0\u03a8\0\u0240\0\u0240\0\u0240\0\u0240"+
    "\0\u0240\0\u03cc\0\u03f0\0\u0240\0\u0414\0\u0438\0\u0240\0\u045c"+
    "\0\u0240\0\u0240\0\u0480\0\u04a4\0\u04c8\0\u0240\0\u04ec\0\u0510"+
    "\0\u03a8\0\u0240\0\u0240\0\u0534\0\u0240\0\u0240\0\u0240\0\u0558"+
    "\0\u057c\0\u0240\0\u03a8\0\u05a0\0\u05c4\0\u05e8\0\u060c\0\u0630"+
    "\0\u0240\0\u0240\0\u0654\0\u0678\0\u0240\0\u069c\0\u06c0\0\u0534"+
    "\0\u0240\0\u06e4\0\u0708\0\u072c\0\u0750\0\u0774\0\u0798\0\u07bc"+
    "\0\u07e0\0\u0804\0\u072c\0\u0828\0\u0678\0\u084c\0\u0870\0\u0558"+
    "\0\u0240\0\u0894\0\u08b8\0\u08dc\0\u0900\0\u0924\0\u0948\0\u096c"+
    "\0\u0990\0\u09b4\0\u09d8\0\u09fc\0\u0a20\0\u0a44\0\u0a68\0\u0a8c"+
    "\0\u0ab0\0\u0ad4\0\u0af8\0\u0b1c\0\u0b40\0\u0b64\0\u0b88\0\u0bac"+
    "\0\u0bd0\0\u0bf4\0\u0c18\0\u0c3c\0\u0c60\0\u0c84\0\u0ca8\0\u0ccc"+
    "\0\u0cf0\0\u0d14\0\u0d38\0\u0d5c\0\u0d80\0\u0da4\0\u0dc8\0\u0dec"+
    "\0\u0e10\0\u0d14\0\u0d38";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[139];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\4\21\1\22\11\21\1\23\1\24\1\25\7\21\1\26"+
    "\7\21\1\27\2\21\1\26\1\21\1\30\15\21\1\31"+
    "\35\21\1\32\4\21\1\23\1\31\1\21\10\32\4\21"+
    "\2\32\2\21\3\32\11\21\1\33\4\21\1\23\1\31"+
    "\1\21\10\33\4\21\2\33\2\21\3\33\2\21\1\34"+
    "\13\21\1\35\1\31\42\21\1\35\1\36\1\37\34\21"+
    "\1\40\4\21\1\35\1\41\1\42\10\40\4\21\2\40"+
    "\2\21\3\40\2\21\1\43\13\21\1\35\1\44\1\45"+
    "\23\21\3\46\1\47\1\50\2\46\1\51\1\46\1\52"+
    "\3\46\1\53\1\54\1\55\1\46\10\52\1\56\1\57"+
    "\1\46\1\60\2\52\2\46\3\52\3\46\1\61\1\50"+
    "\2\46\1\51\1\62\1\63\1\46\1\62\1\46\1\64"+
    "\1\35\1\55\1\46\10\63\1\65\1\66\1\46\1\67"+
    "\2\63\2\46\3\63\3\46\1\47\1\50\2\46\1\51"+
    "\1\46\1\52\3\46\1\53\1\54\1\36\1\37\10\52"+
    "\1\56\1\57\1\46\1\60\2\52\2\46\3\52\11\46"+
    "\1\70\5\46\1\71\1\45\10\70\4\46\2\70\2\46"+
    "\3\70\2\72\1\73\1\74\1\72\1\75\1\76\1\77"+
    "\1\72\1\100\3\72\1\101\1\102\1\103\1\45\1\104"+
    "\3\100\1\105\3\100\1\106\1\107\2\72\2\100\2\72"+
    "\3\100\17\72\1\110\42\72\1\102\1\103\1\45\25\72"+
    "\1\73\1\74\1\72\1\111\1\76\1\112\1\72\1\100"+
    "\3\72\1\101\1\102\1\103\1\45\1\104\3\100\1\105"+
    "\3\100\1\106\1\107\2\72\2\100\2\72\3\100\64\0"+
    "\1\25\64\0\1\113\16\0\1\114\47\0\1\21\34\0"+
    "\1\115\7\0\10\115\4\0\2\115\2\0\3\115\1\0"+
    "\1\33\7\0\1\33\7\0\12\33\2\0\2\33\2\0"+
    "\3\33\20\0\1\37\24\0\1\40\7\0\1\40\7\0"+
    "\12\40\2\0\2\40\2\0\3\40\20\0\1\42\43\0"+
    "\1\45\34\0\1\52\7\0\10\52\4\0\2\52\2\0"+
    "\3\52\31\0\1\56\1\57\31\0\1\46\54\0\2\56"+
    "\37\0\1\116\26\0\1\63\7\0\10\63\4\0\2\63"+
    "\2\0\3\63\31\0\1\65\1\66\42\0\2\65\37\0"+
    "\1\117\16\0\1\70\7\0\1\70\7\0\12\70\2\0"+
    "\2\70\2\0\3\70\1\0\2\120\1\121\13\120\2\0"+
    "\12\120\2\0\2\120\1\0\4\120\1\0\1\100\7\0"+
    "\1\100\7\0\12\100\2\0\2\100\2\0\3\100\31\0"+
    "\1\106\1\107\12\0\1\100\7\0\1\100\7\0\1\100"+
    "\1\122\10\100\2\0\2\100\2\0\3\100\1\0\1\100"+
    "\7\0\1\100\7\0\5\100\1\123\4\100\2\0\2\100"+
    "\2\0\3\100\10\0\1\124\20\0\2\106\21\0\1\124"+
    "\53\0\1\72\47\0\1\125\17\0\14\114\1\126\3\114"+
    "\1\0\23\114\16\0\1\127\43\0\1\130\26\0\1\100"+
    "\7\0\1\100\7\0\2\100\1\131\7\100\2\0\2\100"+
    "\2\0\3\100\1\0\1\100\7\0\1\100\7\0\6\100"+
    "\1\132\3\100\2\0\2\100\2\0\3\100\31\0\2\133"+
    "\53\0\1\134\1\0\14\114\1\126\3\114\1\0\17\114"+
    "\1\135\3\114\16\0\1\127\16\0\1\136\24\0\1\130"+
    "\16\0\1\137\7\0\1\100\7\0\1\100\7\0\3\100"+
    "\1\140\6\100\2\0\2\100\2\0\3\100\1\0\1\100"+
    "\7\0\1\100\7\0\7\100\1\131\2\100\2\0\2\100"+
    "\2\0\3\100\24\0\1\141\41\0\1\142\43\0\1\143"+
    "\45\0\1\144\43\0\1\145\44\0\1\146\43\0\1\147"+
    "\24\0\1\150\43\0\1\151\70\0\1\152\43\0\1\153"+
    "\10\0\20\152\1\0\12\152\1\154\10\152\20\153\1\0"+
    "\12\153\1\155\10\153\16\152\1\156\1\152\1\0\12\152"+
    "\1\154\10\152\16\153\1\157\1\153\1\0\12\153\1\155"+
    "\10\153\16\152\1\156\1\152\1\0\1\160\11\152\1\154"+
    "\10\152\16\153\1\157\1\153\1\0\1\161\11\153\1\155"+
    "\10\153\20\152\1\0\5\152\1\162\4\152\1\154\10\152"+
    "\20\153\1\0\5\153\1\163\4\153\1\155\10\153\20\152"+
    "\1\0\1\152\1\164\10\152\1\154\10\152\20\153\1\0"+
    "\1\153\1\165\10\153\1\155\10\153\20\152\1\0\12\152"+
    "\1\154\2\152\1\166\5\152\20\153\1\0\12\153\1\155"+
    "\2\153\1\167\5\153\20\152\1\0\3\152\1\170\6\152"+
    "\1\154\10\152\20\153\1\0\3\153\1\171\6\153\1\155"+
    "\10\153\20\152\1\0\1\172\11\152\1\154\10\152\20\153"+
    "\1\0\1\173\11\153\1\155\10\153\6\152\1\174\11\152"+
    "\1\0\12\152\1\154\10\152\6\153\1\175\11\153\1\0"+
    "\12\153\1\155\10\153\20\152\1\0\12\152\1\176\10\152"+
    "\20\153\1\0\12\153\1\177\10\153\20\176\1\0\12\176"+
    "\1\200\10\176\20\177\1\0\12\177\1\201\10\177\20\176"+
    "\1\0\12\176\1\200\3\176\1\202\4\176\20\177\1\0"+
    "\12\177\1\201\3\177\1\203\4\177\20\202\1\0\13\202"+
    "\1\204\7\202\20\203\1\0\13\203\1\205\7\203\20\202"+
    "\1\0\13\202\1\204\3\202\1\206\3\202\20\203\1\0"+
    "\13\203\1\205\3\203\1\207\3\203\20\202\1\0\5\202"+
    "\1\210\5\202\1\204\7\202\20\203\1\0\5\203\1\211"+
    "\5\203\1\205\7\203\20\202\1\0\13\202\1\204\2\202"+
    "\1\212\4\202\20\203\1\0\13\203\1\205\2\203\1\213"+
    "\4\203";

  private static int [] zzUnpackTrans() {
    int [] result = new int[3636];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\20\0\3\11\1\1\1\11\2\1\1\11\3\1\2\11"+
    "\1\1\1\11\2\1\2\11\1\1\5\11\2\1\1\11"+
    "\2\1\1\11\1\1\2\11\3\1\1\11\3\1\2\11"+
    "\1\1\3\11\2\1\1\11\6\1\2\11\2\0\1\11"+
    "\3\0\1\11\2\1\5\0\3\1\1\0\1\1\2\0"+
    "\1\1\1\11\50\0\2\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[139];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
    // Reset The Token Scanner 
    @Override
    public final void init(
            final Reader reader,
            final boolean comment,
            final boolean newline,
            final boolean whitespace) {
        // Reset The Internal Stuff
        yyreset(reader);
        // Initialize The Comment Flag
        mComment = comment;
        // Initialize The Comment Flag
        mNewline = newline; //comment
        // Initialize The Whitespace Flag
        mWhiteSpace = whitespace;
        // Reset  Token Object Index
        mTokenIndex = -1;
        // Reset  Last Type Of Token
        mLastToken = -1;
        // Reset  Last Lexxer State
        mLastState = YYINITIAL;
    }
    // The Token Scanner Construction
    public ScriptLexxer(
        Reader reader,
        boolean comment, 
        boolean newline,
        boolean whitespace) {
        // Initialize The Reader
        init(
            reader,
            comment,
            newline,
            whitespace);
    }
    // The Token Scanner Construction
    public ScriptLexxer(
        boolean comment,
        boolean newline,
        boolean whitespace) {
        // Initialize The Reader
        init(
            null,
            comment,
            newline,
            whitespace);
    }
    // Creating A New Token
    public final ScriptSymbol create(final int type) {
        // Set The Last Token Type
        mLastToken = type;
        // Increment The Token Index
        ++mTokenIndex;
        // Compute The Field Value
        final String field = ScriptSymbol.getField(type);
        // Compute The State Value
        final String lexic = ScriptSymbol.getState(yystate());
        // Create The New Token
        final SyntaxDocToken token = new SyntaxDocToken(
            mTokenIndex,    // Set The Index
            yystate(),      // Set The State
            type,           // Set The Token
            yychar,         // Set Left Bound
            yychar + yytext().length(), 
            yyline,         // Set Line
            yycolumn,       // Set Column
            yytext(),       // Set Content
            field,          // Set Field
            lexic);         // Set State
        // Create The New Symbol
        final ScriptSymbol symbol = 
            new ScriptSymbol(type, yychar, yychar + yytext().length(), token);
        // Print Some Information
        //mLogger.message("Scanning Symbol " + symbol + "");    
        // Return The New Symbol
        return symbol;
    }


  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public ScriptLexxer(java.io.Reader in) {
      // Do nothing here
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public ScriptLexxer(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 192) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzCurrentPos*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = zzReader.read(zzBuffer, zzEndRead,
                                            zzBuffer.length-zzEndRead);

    if (numRead > 0) {
      zzEndRead+= numRead;
      return false;
    }
    // unlikely but not impossible: read 0 characters, but not at end of stream    
    if (numRead == 0) {
      int c = zzReader.read();
      if (c == -1) {
        return true;
      } else {
        zzBuffer[zzEndRead++] = (char) c;
        return false;
      }     
    }

	// numRead < 0
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
        // Do Nothing
  yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public ScriptSymbol next_token() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      yychar+= zzMarkedPosL-zzStartRead;

      boolean zzR = false;
      for (zzCurrentPosL = zzStartRead; zzCurrentPosL < zzMarkedPosL;
                                                             zzCurrentPosL++) {
        switch (zzBufferL[zzCurrentPosL]) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn++;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 15: 
          { //
    yybegin(YY_TURN_BODY);
    // Return The Abbreviation Token
    return create(ScriptFields.ABBREVIATION);
          }
        case 40: break;
        case 2: 
          { // Enter The Action Scope
    yybegin(YY_ACTION_ACTIVITY);
          }
        case 41: break;
        case 25: 
          { return create(ScriptFields.COLONMARK);
          }
        case 42: break;
        case 11: 
          { // Enter The Turn Head Scope
    yybegin(YYINITIAL);
    // Return The Turn Head Token
    if(mWhiteSpace) {
        // Return The Scene Lang Token
        return create(ScriptFields.NEWLINE);
    }
          }
        case 43: break;
        case 26: 
          { // Leave The Action Tag Scope 
    yybegin(YY_TURN_BODY);
    // Return The New Token Object
    return create(ScriptFields.ACTIONFOOT);
          }
        case 44: break;
        case 19: 
          { //
    yybegin(YY_TURN_BODY);
    if(mWhiteSpace) {
        // Return The Scene Lang Token
        return create(ScriptFields.WHITESPACE);
    }
          }
        case 45: break;
        case 13: 
          { if(mWhiteSpace) {
        return create(ScriptFields.NEWLINE);
    }
          }
        case 46: break;
        case 6: 
          { // Enter The Scene Lang Scope
    //yybegin(YY_SCENE_NAME);
    yybegin(YY_SCENE_BODY);
    // Return The Scene Name Token
    return create(ScriptFields.IDENTIFIER);
          }
        case 47: break;
        case 4: 
          { if(mWhiteSpace) {
        // Return The Newline Token
        return create(ScriptFields.NEWLINE);
    }
          }
        case 48: break;
        case 39: 
          { // Enter The Scene Head Scope
    //yybegin(YY_SCENE_HEAD);
    yybegin(YY_SCENE_UNDL);

    // Return The Scene Head Token
    return create(ScriptFields.SCENEWORD);
          }
        case 49: break;
        case 18: 
          { //
    yybegin(YY_TURN_BODY);
    // Return The Simple Word Token
    return create(ScriptFields.SIMPLEWORD);
          }
        case 50: break;
        case 12: 
          { // Enter The Turn Head Scope
    yybegin(YY_TURN_INIT);
    // Return The Turn Head Token
    return create(ScriptFields.COLONMARK);
          }
        case 51: break;
        case 36: 
          { return create(ScriptFields.FLOATING);
          }
        case 52: break;
        case 7: 
          { // Enter The Scene Body Scope
    yybegin(YY_SCENE_BODY);
    // Return The Scene Foot Token
    return create(ScriptFields.COLONMARK);
          }
        case 53: break;
        case 37: 
          { if(mComment) {
        // Return The Comment Token
        return create(ScriptFields.COMMENT);
    }
          }
        case 54: break;
        case 22: 
          { // Return The Simple Word Token
    return create(ScriptFields.SIMPLEWORD);
          }
        case 55: break;
        case 5: 
          { // Enter The Scene Undl Scope
    yybegin(YY_SCENE_UNDL);
    // Return The Scene Name Token
    return create(ScriptFields.UNDERLINE);
          }
        case 56: break;
        case 34: 
          { // Enter The Scene Lang Scope
    yybegin(YY_SCENE_LANG);
    // Return The Scene Lang Token
    return create(ScriptFields.LANGUAGE);
          }
        case 57: break;
        case 28: 
          { // Enter The Placeholder Scope 
    yybegin(YY_VARIABLE);
    // Remember The Last Lexxer State
    mLastState = YY_ACTION_BODY;
    // Return The Parameter Token 
    return create(ScriptFields.PLACEHOLDER);
          }
        case 58: break;
        case 35: 
          { return create(ScriptFields.SQSTRING);
          }
        case 59: break;
        case 29: 
          { return create(ScriptFields.IDENTIFIER);
          }
        case 60: break;
        case 10: 
          { // Enter The Error State Scope
    yybegin(YY_TURN_NAME);
    // Return The Turn Head Token
    return create(ScriptFields.IDENTIFIER);
          }
        case 61: break;
        case 23: 
          { // Leave The Placeholder Scope 
    yybegin(mLastState);
    // Return The Parameter Token 
    return create(ScriptFields.VARIABLE);
          }
        case 62: break;
        case 32: 
          { // Leave The Action Tag Scope 
    yybegin(YYINITIAL);
          }
        case 63: break;
        case 9: 
          { // Enter The Turn Head Scope
    yybegin(YY_TURN_HEAD);
    // Return The Turn Head Token
    if(mWhiteSpace) {
        // Return The Scene Lang Token
        return create(ScriptFields.NEWLINE);
    }
          }
        case 64: break;
        case 8: 
          { if(mWhiteSpace) {
        // Return The Scene Lang Token
        return create(ScriptFields.WHITESPACE);
    }
          }
        case 65: break;
        case 24: 
          { // Create An Error Here
    return create(ScriptFields.ERRORSTATE);
          }
        case 66: break;
        case 30: 
          { if(mWhiteSpace) {
        return create(ScriptFields.WHITESPACE);
    }
          }
        case 67: break;
        case 20: 
          { // Return The Abbreviation Token
    return create(ScriptFields.ABBREVIATION);
          }
        case 68: break;
        case 1: 
          { // Enter The Error State Scope
    yybegin(YY_ERROR_STATE);
    // Return The Scene Head Token
    return create(ScriptFields.ERRORSTATE);
          }
        case 69: break;
        case 21: 
          { // Enter The Action Tag Scope
    yybegin(YY_TURN_FOOT);
    // Return The Punctuation Token
    return create(ScriptFields.PUNCTUATION);
          }
        case 70: break;
        case 27: 
          { return create(ScriptFields.ASSIGNMENT);
          }
        case 71: break;
        case 33: 
          { // Enter The Placeholder Scope 
    yybegin(YY_VARIABLE);
    // Remember The Last Lexxer State
    mLastState = YY_ACTION_ACTIVITY;
    // Return The Parameter Token 
    return create(ScriptFields.PLACEHOLDER);
          }
        case 72: break;
        case 31: 
          { return create(ScriptFields.INTEGER);
          }
        case 73: break;
        case 3: 
          { if(mWhiteSpace) {
        // Return The Whitespace Token
        return create(ScriptFields.WHITESPACE);
    }
          }
        case 74: break;
        case 17: 
          { // Enter The Placeholder Scope 
    yybegin(YY_VARIABLE);
    // Remember The Last Lexxer State
    mLastState = YY_TURN_BODY;
    // Return The Parameter Token 
    return create(ScriptFields.PLACEHOLDER);
          }
        case 75: break;
        case 16: 
          { // Enter The Action Tag Scope
    yybegin(YY_ACTION_BODY);
    // Return The New Token Object
    return create(ScriptFields.ACTIONHEAD);
          }
        case 76: break;
        case 38: 
          { return create(ScriptFields.BOOLEAN);
          }
        case 77: break;
        case 14: 
          { // Leave The Placeholder Scope 
    yybegin(YY_ERROR_STATE);
    // Create An Error Here
    return create(ScriptFields.ERRORSTATE);
          }
        case 78: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
              {     // Return NULL At End Of File
    return null;
    // Return EOF Token At File End                                   
    //return create(ScriptFields.EOF);
 }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
