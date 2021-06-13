/*
 * Scores.java
 *
 * Created on May 12, 2007, 11:00 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package Scores;

/**
 *
 * @author Administrator
 */
public class Scores implements java.io.Serializable {
	private String strPlayer1;
	private String strPlayer2;

	private int player1Set1, player2Set1;
	private int player1Set2, player2Set2;
	private int player1Set3, player2Set3;

	public String getStrPlayer(int num)
	{
		switch(num) {
		case 1: return strPlayer1;
		case 2: return strPlayer2;
		default:
			break;
		}
		return null;
	}

	public int getPlayerSet(int num,int set)
	{
		switch(num) {
		case 1:
			switch(set) {
			case 1: return player1Set1;
			case 2: return player1Set2;
			case 3: return player1Set3;
			default:break;
			}
			break;
		case 2:
			switch(set) {
			case 1: return player2Set1;
			case 2: return player2Set2;
			case 3: return player2Set3;
			default:break;
			}
			break;
		default:
			break;
		}
		return 0;
	}
	
	public Scores() {
		strPlayer1 = "";
		strPlayer2 = "";

		player1Set1 = 0;
		player2Set1 = 0;

		player1Set2 = 0;
		player2Set2 = 0;

		player1Set3 = 0;
		player2Set3 = 0;
	}

	public void setNames(String strPlayer1, String strPlayer2) {
		this.strPlayer1 = strPlayer1;
		this.strPlayer2 = strPlayer2;
	}

	public void setSet1Score(int player1Set1, int player2Set1) {
		this.player1Set1 = player1Set1;
		this.player2Set1 = player2Set1;
	}

	public void setSet2Score(int player1Set2, int player2Set2) {
		this.player1Set2 = player1Set2;
		this.player2Set2 = player2Set2;
	}

	public void setSet3Score(int player1Set3, int player2Set3) {
		this.player1Set3 = player1Set3;
		this.player2Set3 = player2Set3;
	}
}

