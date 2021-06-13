/*
 * ScoresInterface.java
 *
 * These program create a Remote Interface for an RMI application
 *
 * Copyright (c) 2007 Aptech Software Limited. All Rights Reserved.
 */

package Scores;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Vincent
 */
public interface RmiSampleInterface extends Remote {
    public Scores getScore() throws RemoteException;
	public String getPlayer(int i) throws RemoteException;
}
