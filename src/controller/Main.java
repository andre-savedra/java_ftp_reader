package controller;

import java.awt.EventQueue;

import vision.Viewer;


public class Main {


	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Viewer myView = new Viewer();
					myView.setVisible(true);								


				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		

	}

}
