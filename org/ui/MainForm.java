package org.ui;

import com.core.ApplicationContext;
import com.core.module.webshell.godzilla.imp.Cryption;
import com.core.util.functions;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainForm extends JFrame {
  private JPanel root;
  
  private JTabbedPane goziila_tabbedPane;
  
  private JPanel Test_Jpanel;
  
  private JTextField pass_textField;
  
  private JTextField key_textField;
  
  private JCheckBox key_checkBox;
  
  private JComboBox payload_comboBox;
  
  private JComboBox crypt_comboBox;
  
  private JLabel pass_Label;
  
  private JLabel key_JLabel;
  
  private JLabel payload_JLabel;
  
  private JLabel crypt_Label;
  
  private JPanel goziila_Jpanel;
  
  private JTabbedPane decode_tabbedPane;
  
  private JPanel request_Jpanel;
  
  private JPanel response_Jpanel;
  
  private JTextArea req_in_textArea;
  
  private JButton req_decode_button;
  
  private JTextArea req_out_textArea;
  
  private JTextArea resp_in_textArea;
  
  private JButton resp_decode_button;
  
  private JTextArea resp_out_textArea;
  
  public MainForm() {
    this.payload_comboBox.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            String selectedItem = (String)MainForm.this.payload_comboBox.getSelectedItem();
            switch (selectedItem) {
              case "JavaDynamicPayload":
                MainForm.this.crypt_comboBox.removeAllItems();
                MainForm.this.crypt_comboBox.addItem("JAVA_AES_BASE64");
                MainForm.this.crypt_comboBox.addItem("JAVA_AES_RAW");
                break;
              case "AspDynamicPayload":
                MainForm.this.crypt_comboBox.removeAllItems();
                MainForm.this.crypt_comboBox.addItem("ASP_EVAL_BASE64");
                MainForm.this.crypt_comboBox.addItem("ASP_XOR_BASE64");
                MainForm.this.crypt_comboBox.addItem("ASP_RAW");
                MainForm.this.crypt_comboBox.addItem("ASP_BASE64");
                MainForm.this.crypt_comboBox.addItem("ASP_XOR_RAW");
                break;
              case "CSharpDynamicPayload":
                MainForm.this.crypt_comboBox.removeAllItems();
                MainForm.this.crypt_comboBox.addItem("CSHARP_ASMX_AES_BASE64");
                MainForm.this.crypt_comboBox.addItem("CSHARP_EVAL_AES_BASE64");
                MainForm.this.crypt_comboBox.addItem("CSHARP_AES_BASE64");
                MainForm.this.crypt_comboBox.addItem("CSHARP_AES_RAW");
                break;
              case "PhpDynamicPayload":
                MainForm.this.crypt_comboBox.removeAllItems();
                MainForm.this.crypt_comboBox.addItem("PHP_EVAL_XOR_BASE64");
                MainForm.this.crypt_comboBox.addItem("PHP_XOR_RAW");
                MainForm.this.crypt_comboBox.addItem("PHP_XOR_BASE64");
                break;
            } 
          }
        });
    this.req_decode_button.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            try {
              String payloadName = (String)MainForm.this.payload_comboBox.getSelectedItem();
              String cryptionName = (String)MainForm.this.crypt_comboBox.getSelectedItem();
              String key = MainForm.this.key_textField.getText().trim();
              String pass = MainForm.this.pass_textField.getText().trim();
              boolean isKeyEncrypted = MainForm.this.key_checkBox.isSelected();
              if (!isKeyEncrypted)
                key = functions.getSecretKeyX(key); 
              Cryption cryption = ApplicationContext.getCryption(payloadName, cryptionName);
              String requestEncodeText = MainForm.this.req_in_textArea.getText().trim();
              String requestDecodeText = cryption.decodeRequest(requestEncodeText.getBytes(StandardCharsets.UTF_8), pass, key);
              MainForm.this.req_out_textArea.setText(requestDecodeText);
            } catch (Exception e1) {
              e1.printStackTrace();
            } 
          }
        });
    this.resp_decode_button.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            try {
              String payloadName = (String)MainForm.this.payload_comboBox.getSelectedItem();
              String cryptionName = (String)MainForm.this.crypt_comboBox.getSelectedItem();
              String key = MainForm.this.key_textField.getText().trim();
              String pass = MainForm.this.pass_textField.getText().trim();
              boolean isKeyEncrypted = MainForm.this.key_checkBox.isSelected();
              if (!isKeyEncrypted)
                key = functions.getSecretKeyX(key); 
              Cryption cryption = ApplicationContext.getCryption(payloadName, cryptionName);
              String respEncodeText = MainForm.this.resp_in_textArea.getText().trim();
              String respDecodeText = cryption.decodeResponse(respEncodeText.getBytes(StandardCharsets.UTF_8), pass, key);
              MainForm.this.resp_out_textArea.setText(respDecodeText);
            } catch (Exception e2) {
              e2.printStackTrace();
            } 
          }
        });
  }
  
  public static void init(JFrame frame) {
    ApplicationContext.init();
    MainForm mainForm = new MainForm();
    mainForm.pass_Label = new JLabel("aaaa");
    frame.add(mainForm.pass_Label);
    frame.setContentPane(mainForm.root);
    frame.setDefaultCloseOperation(3);
    frame.setSize(1400, 900);
    frame.setVisible(true);
  }
  
  public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    System.setProperty("file.encoding", "UTF-8");
    Font globalFont = new Font("宋体", 0, 20);
    UIManager.put("Label.font", globalFont);
    UIManager.put("CheckBox.font", globalFont);
    UIManager.put("Button.font", globalFont);
    UIManager.put("ComboBox.font", globalFont);
    UIManager.put("TabbedPane.font", globalFont);
    UIManager.put("TextArea.font", globalFont);
    JFrame frame = new JFrame("webshell解密工具");
    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    SwingUtilities.updateComponentTreeUI(frame);
    init(frame);
  }
}
