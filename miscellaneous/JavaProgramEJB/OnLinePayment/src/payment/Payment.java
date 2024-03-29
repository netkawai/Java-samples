/*
 * Payment.java
 *
 * This program demonstrate how to use formattedtextfield.
 *
 *  Copyright (c) 2006 Aptech Software Limited. All Rights Reserved.
 */

package payment;

import java.awt.Color;
import java.util.Calendar;
import javax.swing.JFormattedTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author Vincent
 *
 */
public class Payment extends javax.swing.JFrame {
  
  /** Creates new form Payment */
  public Payment() {
    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } catch (IllegalAccessException ex) {
      ex.printStackTrace();
    } catch (UnsupportedLookAndFeelException ex) {
      ex.printStackTrace();
    } catch (ClassNotFoundException ex) {
      ex.printStackTrace();
    } catch (InstantiationException ex) {
      ex.printStackTrace();
    }
    UIManager.put("Button.showMnemonics",Boolean.TRUE);
    getContentPane().setBackground(new Color(212,208,200));
    initComponents();
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
  private void initComponents() {
    pnlHeading = new javax.swing.JPanel();
    lblHeader = new javax.swing.JLabel();
    pnlPayment = new javax.swing.JPanel();
    lblName = new javax.swing.JLabel();
    txtName = new javax.swing.JTextField();
    lblNameOnCard = new javax.swing.JLabel();
    lblCardNumber = new javax.swing.JLabel();
    txfCardNumber = new javax.swing.JFormattedTextField();
    lblExpiryDate = new javax.swing.JLabel();
    txfExpiryDate = new javax.swing.JFormattedTextField();
    lblDateFormat = new javax.swing.JLabel();
    pnlSecurityCode = new javax.swing.JPanel();
    jLabel3 = new javax.swing.JLabel();
    lblCode = new javax.swing.JLabel();
    txtNumber = new javax.swing.JTextField();
    lblSecurityCode = new javax.swing.JLabel();
    btnSubmit = new javax.swing.JButton();
    btnCancel = new javax.swing.JButton();
    scpStatus = new javax.swing.JScrollPane();
    txaStatus = new javax.swing.JTextArea();

    getContentPane().setLayout(new java.awt.FlowLayout());

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Online Payment");
    setResizable(false);
    pnlHeading.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    pnlHeading.setPreferredSize(new java.awt.Dimension(350, 40));
    lblHeader.setFont(new java.awt.Font("Tahoma", 0, 20));
    lblHeader.setText("Welcome to OnLine Payments");
    lblHeader.setPreferredSize(new java.awt.Dimension(270, 25));
    pnlHeading.add(lblHeader);

    getContentPane().add(pnlHeading);

    pnlPayment.setBorder(javax.swing.BorderFactory.createTitledBorder("Payment Details"));
    pnlPayment.setPreferredSize(new java.awt.Dimension(350, 150));
    lblName.setText("Name:");
    lblName.setPreferredSize(new java.awt.Dimension(100, 20));
    pnlPayment.add(lblName);

    txtName.setPreferredSize(new java.awt.Dimension(200, 20));
    pnlPayment.add(txtName);

    lblNameOnCard.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    lblNameOnCard.setText("Enter the name as it appears on the card.");
    lblNameOnCard.setPreferredSize(new java.awt.Dimension(300, 20));
    pnlPayment.add(lblNameOnCard);

    lblCardNumber.setText("Credit Card Number:");
    lblCardNumber.setPreferredSize(new java.awt.Dimension(100, 20));
    pnlPayment.add(lblCardNumber);

    try{
      txfCardNumber = new JFormattedTextField(new MaskFormatter("####-####-####-####"));
    }catch (java.text.ParseException e) {
      e.printStackTrace();
    }
    txfCardNumber.setPreferredSize(new java.awt.Dimension(200, 20));
    pnlPayment.add(txfCardNumber);

    lblExpiryDate.setText("Expiry Date:");
    lblExpiryDate.setPreferredSize(new java.awt.Dimension(100, 20));
    pnlPayment.add(lblExpiryDate);

    try{
      txfExpiryDate = new JFormattedTextField(new MaskFormatter("##/####"));
    }catch (java.text.ParseException e) {
      e.printStackTrace();
    }
    txfExpiryDate.setPreferredSize(new java.awt.Dimension(100, 20));
    pnlPayment.add(txfExpiryDate);

    lblDateFormat.setText("(mm/yyyy)");
    lblDateFormat.setPreferredSize(new java.awt.Dimension(100, 20));
    pnlPayment.add(lblDateFormat);

    getContentPane().add(pnlPayment);

    pnlSecurityCode.setBorder(javax.swing.BorderFactory.createTitledBorder("Security Code"));
    pnlSecurityCode.setPreferredSize(new java.awt.Dimension(350, 120));
    jLabel3.setText("Enter the number as seen in the label on the right.");
    jLabel3.setPreferredSize(new java.awt.Dimension(250, 20));
    pnlSecurityCode.add(jLabel3);

    lblCode.setText("Code:");
    lblCode.setPreferredSize(new java.awt.Dimension(100, 20));
    pnlSecurityCode.add(lblCode);

    txtNumber.setPreferredSize(new java.awt.Dimension(100, 20));
    pnlSecurityCode.add(txtNumber);

    lblSecurityCode.setText("Sh4Ga7");
    lblSecurityCode.setPreferredSize(new java.awt.Dimension(80, 20));
    pnlSecurityCode.add(lblSecurityCode);

    btnSubmit.setText("Submit");
    btnSubmit.setPreferredSize(new java.awt.Dimension(70, 23));
    btnSubmit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSubmitActionPerformed(evt);
      }
    });

    pnlSecurityCode.add(btnSubmit);

    btnCancel.setMnemonic('C');
    btnCancel.setText("Cancel");
    btnCancel.setPreferredSize(new java.awt.Dimension(70, 23));
    btnCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnCancelActionPerformed(evt);
      }
    });

    pnlSecurityCode.add(btnCancel);

    getContentPane().add(pnlSecurityCode);

    scpStatus.setPreferredSize(new java.awt.Dimension(350, 115));
    txaStatus.setColumns(20);
    txaStatus.setEditable(false);
    txaStatus.setForeground(new java.awt.Color(204, 0, 0));
    txaStatus.setRows(5);
    txaStatus.setOpaque(false);
    scpStatus.setViewportView(txaStatus);

    getContentPane().add(scpStatus);

    java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((screenSize.width-402)/2, (screenSize.height-486)/2, 402, 486);
  }// </editor-fold>//GEN-END:initComponents
  
  private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
// TODO add your handling code here:
    boolean flag = true;
    String message = " Enter the following details(s) correctly: \n";
    
    if (txtName.getText().length() == 0) {
      flag = false;
      message += " Name \n";
    }
    
    if (!(txfCardNumber.isEditValid())) {
      message += " Credit Card Number \n";
      flag = false;
    }
    
    if(txfExpiryDate.isEditValid()) {
      String strDate = txfExpiryDate.getText();
      int month = Integer.parseInt(strDate.substring(0,strDate.indexOf("/")));
      int year = Integer.parseInt(strDate.substring(strDate.lastIndexOf("/")+1));
      
      if(month > 12 || month <= 0) {
        flag = false;
        message += " Month of Expiry \n";
      }
      
      if(year < Calendar.getInstance().get(1)) {
        flag = false;
        message += " Year of Expiry \n";
      }
      
      if(year == Calendar.getInstance().get(1)) {
        if (month <= Calendar.getInstance().get(2)) {
          flag = false;
          message += " Card Expired \n";
        }
      }else {
        flag = false;
        message += " Expiry Date \n";
      }
    }
    if(!(txtNumber.getText().equals("Sh4Ga7"))) {
      flag = false;
      message += " Security Number \n";
    }
    
    if(flag)
      txaStatus.setText("Thank you! Your order will be shipped in three days. ");
    else {
      txaStatus.setText(message);
    }
    
  }//GEN-LAST:event_btnSubmitActionPerformed
  
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
// TODO add your handling code here:
      System.exit(0);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
      java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
          new Payment().setVisible(true);
        }
      });
    }
    
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnCancel;
  private javax.swing.JButton btnSubmit;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel lblCardNumber;
  private javax.swing.JLabel lblCode;
  private javax.swing.JLabel lblDateFormat;
  private javax.swing.JLabel lblExpiryDate;
  private javax.swing.JLabel lblHeader;
  private javax.swing.JLabel lblName;
  private javax.swing.JLabel lblNameOnCard;
  private javax.swing.JLabel lblSecurityCode;
  private javax.swing.JPanel pnlHeading;
  private javax.swing.JPanel pnlPayment;
  private javax.swing.JPanel pnlSecurityCode;
  private javax.swing.JScrollPane scpStatus;
  private javax.swing.JTextArea txaStatus;
  private javax.swing.JFormattedTextField txfCardNumber;
  private javax.swing.JFormattedTextField txfExpiryDate;
  private javax.swing.JTextField txtName;
  private javax.swing.JTextField txtNumber;
  // End of variables declaration//GEN-END:variables
  
}
