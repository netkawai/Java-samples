/*
 * ScoresImpl.java
 *
 * This program implements the remote interface ScoresInterface.
 *
 * Copyright (c) 2007 Aptech Software Limited. All Rights Reserved.
 *
 */

package Server;

import java.rmi.server.UnicastRemoteObject;
import Scores.*;

/**
 *
 * @author Vincent
 */

public class RmiSampleImpl extends UnicastRemoteObject implements RmiSampleInterface
{
	private Scores score;

        /** Creates a new instance of ScoresImpl */
	public RmiSampleImpl(Scores score) throws java.rmi.RemoteException {
		super();
		this.score = score;
	}

	public Scores getScore() throws java.rmi.RemoteException {
		return score;
	}	

	public String getPlayer(int i) throws java.rmi.RemoteException {
		return score.getStrPlayer(i);
	}
}
