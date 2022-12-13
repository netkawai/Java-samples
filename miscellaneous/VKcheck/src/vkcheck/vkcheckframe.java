/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vkcheck;

import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author Kawai
 */
public class vkcheckframe extends java.awt.Frame {

    String s;

    public vkcheckframe() {
        Label l=new Label();
        l.setText("Hit any key");
        l.setFont(new Font("Sans",Font.PLAIN,48));
        l.setAlignment(Label.CENTER);
        this.setSize(400,160);
        this.addKeyListener(new KeyM());
        this.add(BorderLayout.CENTER, l);
        // for application termination in close button
        this.addWindowListener(new WindowAdapter(){
        public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }
        });
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new vkcheckframe();
    }


class KeyM extends KeyAdapter {
public void keyReleased(KeyEvent ke) {
int kc=ke.getKeyCode();
switch(kc) {
case KeyEvent.VK_0:s="VK_0";break;
case KeyEvent.VK_1: s="VK_1";break;
case KeyEvent.VK_2: s="VK_2";break;
case KeyEvent.VK_3: s="VK_3";break;
case KeyEvent.VK_4: s="VK_4";break;
case KeyEvent.VK_5: s="VK_5";break;
case KeyEvent.VK_6: s="VK_6";break;
case KeyEvent.VK_7: s="VK_7";break;
case KeyEvent.VK_8: s="VK_8";break;
case KeyEvent.VK_9: s="VK_9";break;
case KeyEvent.VK_A: s="VK_A";break;
case KeyEvent.VK_B: s="VK_B";break;
case KeyEvent.VK_C: s="VK_C";break;
case KeyEvent.VK_D: s="VK_D";break;
case KeyEvent.VK_E: s="VK_E";break;
case KeyEvent.VK_F: s="VK_F";break;
case KeyEvent.VK_G: s="VK_G";break;
case KeyEvent.VK_H: s="VK_H";break;
case KeyEvent.VK_I: s="VK_I";break;
case KeyEvent.VK_J: s="VK_J";break;
case KeyEvent.VK_K: s="VK_K";break;
case KeyEvent.VK_L: s="VK_L";break;
case KeyEvent.VK_M: s="VK_M";break;
case KeyEvent.VK_N: s="VK_N";break;
case KeyEvent.VK_O: s="VK_O";break;
case KeyEvent.VK_P: s="VK_P";break;
case KeyEvent.VK_Q: s="VK_Q";break;
case KeyEvent.VK_R: s="VK_R";break;
case KeyEvent.VK_S: s="VK_S";break;
case KeyEvent.VK_T: s="VK_T";break;
case KeyEvent.VK_U: s="VK_U";break;
case KeyEvent.VK_V: s="VK_V";break;
case KeyEvent.VK_W: s="VK_W";break;
case KeyEvent.VK_X: s="VK_X";break;
case KeyEvent.VK_Y: s="VK_Y";break;
case KeyEvent.VK_Z: s="VK_Z";break;
case KeyEvent.VK_F1: s="VK_F1";break;
case KeyEvent.VK_F2: s="VK_F2";break;
case KeyEvent.VK_F3: s="VK_F3";break;
case KeyEvent.VK_F4: s="VK_F4";break;
case KeyEvent.VK_F5: s="VK_F5";break;
case KeyEvent.VK_F6: s="VK_F6";break;
case KeyEvent.VK_F7: s="VK_F7";break;
case KeyEvent.VK_F8: s="VK_F8";break;
case KeyEvent.VK_F9: s="VK_F9";break;
case KeyEvent.VK_F10: s="VK_F10";break;
case KeyEvent.VK_F11: s="VK_F11";break;
case KeyEvent.VK_F12: s="VK_F12";break;
case KeyEvent.VK_F13: s="VK_F13";break;
case KeyEvent.VK_F14: s="VK_F14";break;
case KeyEvent.VK_F15: s="VK_F15";break;
case KeyEvent.VK_F16: s="VK_F16";break;
case KeyEvent.VK_F17: s="VK_F17";break;
case KeyEvent.VK_F18: s="VK_F18";break;
case KeyEvent.VK_F19: s="VK_F19";break;
case KeyEvent.VK_F20: s="VK_F20";break;
case KeyEvent.VK_F21: s="VK_F21";break;
case KeyEvent.VK_F22: s="VK_F22";break;
case KeyEvent.VK_F23: s="VK_F23";break;
case KeyEvent.VK_F24: s="VK_F24";break;
case KeyEvent.VK_ACCEPT: s="VK_ACCEPT";break;
case KeyEvent.VK_ADD: s="VK_ADD";break;
case KeyEvent.VK_AGAIN: s="VK_AGAIN";break;
case KeyEvent.VK_ALL_CANDIDATES: s="VK_ALL_CANDIDATES";break;
case KeyEvent.VK_ALPHANUMERIC: s="VK_ALPHANUMERIC";break;
case KeyEvent.VK_ALT: s="VK_ALT";break;
case KeyEvent.VK_ALT_GRAPH: s="VK_ALT_GRAPH";break;
case KeyEvent.VK_AMPERSAND: s="VK_AMPERSAND";break;
case KeyEvent.VK_ASTERISK: s="VK_ASTERISK";break;
case KeyEvent.VK_AT: s="VK_AT";break;
case KeyEvent.VK_BACK_QUOTE: s="VK_BACK_QUOTE";break;
case KeyEvent.VK_BACK_SLASH: s="VK_BACK_SLASH";break;
case KeyEvent.VK_BACK_SPACE: s="VK_BACK_SPACE";break;
case KeyEvent.VK_BEGIN: s="VK_BEGIN";break;
case KeyEvent.VK_BRACELEFT: s="VK_BRACELEFT";break;
case KeyEvent.VK_BRACERIGHT: s="VK_BRACERIGHT";break;
case KeyEvent.VK_CANCEL: s="VK_CANCEL";break;
case KeyEvent.VK_CAPS_LOCK: s="VK_CAPS_LOCK";break;
case KeyEvent.VK_CIRCUMFLEX: s="VK_CIRCUMFLEX";break;
case KeyEvent.VK_CLEAR: s="VK_CLEAR";break;
case KeyEvent.VK_CLOSE_BRACKET: s="VK_CLOSE_BRACKET";break;
case KeyEvent.VK_CODE_INPUT: s="VK_CODE_INPUT";break;
case KeyEvent.VK_COLON: s="VK_COLON";break;
case KeyEvent.VK_COMMA: s="VK_COMMA";break;
case KeyEvent.VK_COMPOSE: s="VK_COMPOSE";break;
case KeyEvent.VK_CONTEXT_MENU: s="VK_CONTEXT_MENU";break;
case KeyEvent.VK_CONTROL: s="VK_CONTROL";break;
case KeyEvent.VK_CONVERT: s="VK_CONVERT";break;
case KeyEvent.VK_COPY: s="VK_COPY";break;
case KeyEvent.VK_CUT: s="VK_CUT";break;
case KeyEvent.VK_DEAD_ABOVEDOT: s="VK_DEAD_ABOVEDOT";break;
case KeyEvent.VK_DEAD_ABOVERING: s="VK_DEAD_ABOVERING";break;
case KeyEvent.VK_DEAD_ACUTE: s="VK_DEAD_ACUTE";break;
case KeyEvent.VK_DEAD_BREVE: s="VK_DEAD_BREVE";break;
case KeyEvent.VK_DEAD_CARON: s="VK_DEAD_CARON";break;
case KeyEvent.VK_DEAD_CEDILLA: s="VK_DEAD_CEDILLA";break;
case KeyEvent.VK_DEAD_CIRCUMFLEX: s="VK_DEAD_CIRCUMFLEX";break;
case KeyEvent.VK_DEAD_DIAERESIS: s="VK_DEAD_DIAERESIS";break;
case KeyEvent.VK_DEAD_DOUBLEACUTE: s="VK_DEAD_DOUBLEACUTE";break;
case KeyEvent.VK_DEAD_GRAVE: s="VK_DEAD_GRAVE";break;
case KeyEvent.VK_DEAD_IOTA: s="VK_DEAD_IOTA";break;
case KeyEvent.VK_DEAD_MACRON: s="VK_DEAD_MACRON";break;
case KeyEvent.VK_DEAD_OGONEK: s="VK_DEAD_OGONEK";break;
case KeyEvent.VK_DEAD_SEMIVOICED_SOUND: s="VK_DEAD_SEMIVOICED_SOUND";break;
case KeyEvent.VK_DEAD_TILDE: s="VK_DEAD_TILDE";break;
case KeyEvent.VK_DEAD_VOICED_SOUND: s="VK_DEAD_VOICED_SOUND";break;
case KeyEvent.VK_DECIMAL: s="VK_DECIMAL";break;
case KeyEvent.VK_DELETE: s="VK_DELETE";break;
case KeyEvent.VK_DIVIDE: s="VK_DIVIDE";break;
case KeyEvent.VK_DOLLAR: s="VK_DOLLAR";break;
case KeyEvent.VK_DOWN: s="VK_DOWN";break;
case KeyEvent.VK_END: s="VK_END";break;
case KeyEvent.VK_ENTER: s="VK_ENTER";break;
case KeyEvent.VK_EQUALS: s="VK_EQUALS";break;
case KeyEvent.VK_ESCAPE: s="VK_ESCAPE";break;
case KeyEvent.VK_EURO_SIGN: s="VK_EURO_SIGN";break;
case KeyEvent.VK_EXCLAMATION_MARK: s="VK_EXCLAMATION_MARK";break;
case KeyEvent.VK_FINAL: s="VK_FINAL";break;
case KeyEvent.VK_FIND: s="VK_FIND";break;
case KeyEvent.VK_FULL_WIDTH: s="VK_FULL_WIDTH";break;
case KeyEvent.VK_GREATER: s="VK_GREATER";break;
case KeyEvent.VK_HALF_WIDTH: s="VK_HALF_WIDTH";break;
case KeyEvent.VK_HELP: s="VK_HELP";break;
case KeyEvent.VK_HIRAGANA: s="VK_HIRAGANA";break;
case KeyEvent.VK_HOME: s="VK_HOME";break;
case KeyEvent.VK_INPUT_METHOD_ON_OFF: s="VK_INPUT_METHOD_ON_OFF";break;
case KeyEvent.VK_INSERT: s="VK_INSERT";break;
case KeyEvent.VK_INVERTED_EXCLAMATION_MARK: s="VK_INVERTED_EXCLAMATION_MARK";break;
case KeyEvent.VK_JAPANESE_HIRAGANA: s="VK_JAPANESE_HIRAGANA";break;
case KeyEvent.VK_JAPANESE_KATAKANA: s="VK_JAPANESE_KATAKANA";break;
case KeyEvent.VK_JAPANESE_ROMAN: s="VK_JAPANESE_ROMAN";break;
case KeyEvent.VK_KANA: s="VK_KANA";break;
case KeyEvent.VK_KANA_LOCK: s="VK_KANA_LOCK";break;
case KeyEvent.VK_KANJI: s="VK_KANJI";break;
case KeyEvent.VK_KATAKANA: s="VK_KATAKANA";break;
case KeyEvent.VK_KP_DOWN: s="VK_KP_DOWN";break;
case KeyEvent.VK_KP_LEFT: s="VK_KP_LEFT";break;
case KeyEvent.VK_KP_RIGHT: s="VK_KP_RIGHT";break;
case KeyEvent.VK_KP_UP: s="VK_KP_UP";break;
case KeyEvent.VK_LEFT: s="VK_LEFT";break;
case KeyEvent.VK_LEFT_PARENTHESIS: s="VK_LEFT_PARENTHESIS";break;
case KeyEvent.VK_LESS: s="VK_LESS";break;
case KeyEvent.VK_META: s="VK_META";break;
case KeyEvent.VK_MINUS: s="VK_MINUS";break;
case KeyEvent.VK_MODECHANGE: s="VK_MODECHANGE";break;
case KeyEvent.VK_MULTIPLY: s="VK_MULTIPLY";break;
case KeyEvent.VK_NONCONVERT: s="VK_NONCONVERT";break;
case KeyEvent.VK_NUM_LOCK: s="VK_NUM_LOCK";break;
case KeyEvent.VK_NUMBER_SIGN: s="VK_NUMBER_SIGN";break;
case KeyEvent.VK_NUMPAD0: s="VK_NUMPAD0";break;
case KeyEvent.VK_NUMPAD1: s="VK_NUMPAD1";break;
case KeyEvent.VK_NUMPAD2: s="VK_NUMPAD2";break;
case KeyEvent.VK_NUMPAD3: s="VK_NUMPAD3";break;
case KeyEvent.VK_NUMPAD4: s="VK_NUMPAD4";break;
case KeyEvent.VK_NUMPAD5: s="VK_NUMPAD5";break;
case KeyEvent.VK_NUMPAD6: s="VK_NUMPAD6";break;
case KeyEvent.VK_NUMPAD7: s="VK_NUMPAD7";break;
case KeyEvent.VK_NUMPAD8: s="VK_NUMPAD8";break;
case KeyEvent.VK_NUMPAD9: s="VK_NUMPAD9";break;
case KeyEvent.VK_OPEN_BRACKET: s="VK_OPEN_BRACKET";break;
case KeyEvent.VK_PAGE_DOWN: s="VK_PAGE_DOWN";break;
case KeyEvent.VK_PAGE_UP: s="VK_PAGE_UP";break;
case KeyEvent.VK_PASTE: s="VK_PASTE";break;
case KeyEvent.VK_PAUSE: s="VK_PAUSE";break;
case KeyEvent.VK_PERIOD: s="VK_PERIOD";break;
case KeyEvent.VK_PLUS: s="VK_PLUS";break;
case KeyEvent.VK_PREVIOUS_CANDIDATE: s="VK_PREVIOUS_CANDIDATE";break;
case KeyEvent.VK_PRINTSCREEN: s="VK_PRINTSCREEN";break;
case KeyEvent.VK_PROPS: s="VK_PROPS";break;
case KeyEvent.VK_QUOTE: s="VK_QUOTE";break;
case KeyEvent.VK_QUOTEDBL: s="VK_QUOTEDBL";break;
case KeyEvent.VK_RIGHT: s="VK_RIGHT";break;
case KeyEvent.VK_RIGHT_PARENTHESIS: s="VK_RIGHT_PARENTHESIS";break;
case KeyEvent.VK_ROMAN_CHARACTERS: s="VK_ROMAN_CHARACTERS";break;
case KeyEvent.VK_SCROLL_LOCK: s="VK_SCROLL_LOCK";break;
case KeyEvent.VK_SEMICOLON: s="VK_SEMICOLON";break;
case KeyEvent.VK_SEPARATER: s="VK_SEPARATER";break;
//	 case KeyEvent.VK_SEPARATOR: s="VK_SEPARATOR";break;
case KeyEvent.VK_SHIFT: s="VK_SHIFT";break;
case KeyEvent.VK_SLASH: s="VK_SLASH";break;
case KeyEvent.VK_SPACE: s="VK_SPACE";break;
case KeyEvent.VK_STOP: s="VK_STOP";break;
case KeyEvent.VK_SUBTRACT: s="VK_SUBTRACT";break;
case KeyEvent.VK_TAB: s="VK_TAB";break;
case KeyEvent.VK_UNDEFINED: s="VK_UNDEFINED";break;
case KeyEvent.VK_UNDERSCORE: s="VK_UNDERSCORE";break;
case KeyEvent.VK_UNDO: s="VK_UNDO";break;
case KeyEvent.VK_UP: s="VK_UP";break;
case KeyEvent.VK_WINDOWS: s="VK_WINDOWS";break;
default: s="==unknown==";
}
System.out.println(s+" ("+Integer.toString(kc)+")");
}
}
}
