package org.ui;

import com.core.ApplicationContext;
import com.core.module.webshell.godzilla.imp.Cryption;
import com.core.util.functions;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

public class GodzillaPanel extends JPanel {
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
  
  private JTabbedPane decode_tabbedPane;
  
  private JPanel request_Jpanel;
  
  private JPanel response_Jpanel;
  
  private JTextArea req_in_textArea;
  
  private JButton req_decode_button;
  
  private JTextArea req_out_textArea;
  
  private JTextArea resp_in_textArea;
  
  private JButton resp_decode_button;
  
  private JTextArea resp_out_textArea;
  
  private JScrollPane req_encode_JscrollPane;
  
  private JScrollPane req_decode_JscrollPane;
  
  private JScrollPane resp_encode_JscrollPane;
  
  private JScrollPane resp_decode_JscrollPane;
  
  private static GodzillaPanel GodzillaPanel;
  
  private String[] PAYLOADS_DICT = new String[] { "AspDynamicPayload", "JavaDynamicPayload", "CSharpDynamicPayload", "PhpDynamicPayload" };
  
  public GodzillaPanel() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    init();
    GodzillaPanel = this;
  }
  
  public static GodzillaPanel getGodzillaPanel() {
    return GodzillaPanel;
  }
  
  public void init() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    GridBagLayout gbl = new GridBagLayout();
    setLayout(gbl);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = 1;
    gbc.weightx = 0.0D;
    gbc.insets = new Insets(0, 3, 0, 5);
    this.pass_Label = new JLabel("密码:");
    addComponent(this, this.pass_Label, gbl, gbc);
    gbc.gridwidth = 2;
    gbc.weightx = 1.0D;
    this.pass_textField = new JTextField("pass");
    addComponent(this, this.pass_textField, gbl, gbc);
    gbc.gridwidth = 1;
    gbc.weightx = 0.0D;
    this.key_JLabel = new JLabel("密钥:");
    addComponent(this, this.key_JLabel, gbl, gbc);
    gbc.gridwidth = 2;
    gbc.weightx = 1.0D;
    this.key_textField = new JTextField("key");
    addComponent(this, this.key_textField, gbl, gbc);
    gbc.gridwidth = 1;
    gbc.weightx = 0.0D;
    this.key_checkBox = new JCheckBox("是否为加密后的key |");
    addComponent(this, this.key_checkBox, gbl, gbc);
    this.payload_JLabel = new JLabel("有效载荷：");
    addComponent(this, this.payload_JLabel, gbl, gbc);
    this.payload_comboBox = new JComboBox();
    addToComboBox(this.payload_comboBox, this.PAYLOADS_DICT);
    addComponent(this, this.payload_comboBox, gbl, gbc);
    this.payload_comboBox.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            String selectedItem = (String)GodzillaPanel.GodzillaPanel.payload_comboBox.getSelectedItem();
            GodzillaPanel.GodzillaPanel.crypt_comboBox.removeAllItems();
            GodzillaPanel.this.addToComboBox(GodzillaPanel.this.crypt_comboBox, ApplicationContext.getAllCryption(selectedItem));
          }
        });
    this.crypt_Label = new JLabel("加密器:");
    addComponent(this, this.crypt_Label, gbl, gbc);
    gbc.gridwidth = 0;
    this.crypt_comboBox = new JComboBox();
    addToComboBox(this.crypt_comboBox, new String[] { "ASP_EVAL_BASE64", "ASP_XOR_BASE64", "ASP_RAW", "ASP_BASE64", "ASP_XOR_RAW" });
    addComponent(this, this.crypt_comboBox, gbl, gbc);
    gbc.weightx = 1.0D;
    gbc.weighty = 1.0D;
    this.decode_tabbedPane = new JTabbedPane();
    addComponent(this, this.decode_tabbedPane, gbl, gbc);
    this.request_Jpanel = new JPanel();
    this.request_Jpanel.setLayout(gbl);
    addComponent(this.decode_tabbedPane, this.request_Jpanel, gbl, gbc);
    this.req_in_textArea = new JTextArea();
    this.req_out_textArea = new JTextArea();
    this.req_encode_JscrollPane = new JScrollPane(this.req_in_textArea);
    this.req_decode_JscrollPane = new JScrollPane(this.req_out_textArea);
    this.req_encode_JscrollPane.setBorder(new TitledBorder("请求加密流量"));
    this.req_decode_JscrollPane.setBorder(new TitledBorder("请求解密流量"));
    this.req_decode_button = new JButton("解密请求流量");
    this.req_decode_button.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            try {
              String payloadName = (String)GodzillaPanel.this.payload_comboBox.getSelectedItem();
              String cryptionName = (String)GodzillaPanel.this.crypt_comboBox.getSelectedItem();
              String key = GodzillaPanel.this.key_textField.getText().trim();
              String pass = GodzillaPanel.this.pass_textField.getText().trim();
              boolean isKeyEncrypted = GodzillaPanel.this.key_checkBox.isSelected();
              if (!isKeyEncrypted)
                key = functions.getSecretKeyX(key); 
              Cryption cryption = ApplicationContext.getCryption(payloadName, cryptionName);
              String requestEncodeText = GodzillaPanel.this.req_in_textArea.getText().trim();
              String requestDecodeText = cryption.decodeRequest(requestEncodeText.getBytes(StandardCharsets.UTF_8), pass, key);
              GodzillaPanel.this.req_out_textArea.setText(requestDecodeText);
            } catch (Exception e1) {
              e1.printStackTrace();
            } 
          }
        });
    addComponent(this.request_Jpanel, this.req_encode_JscrollPane, gbl, gbc);
    gbc.weightx = 0.0D;
    gbc.weighty = 0.0D;
    addComponent(this.request_Jpanel, this.req_decode_button, gbl, gbc);
    gbc.weightx = 1.0D;
    gbc.weighty = 1.0D;
    addComponent(this.request_Jpanel, this.req_decode_JscrollPane, gbl, gbc);
    this.decode_tabbedPane.add(this.request_Jpanel, "请求数据解密");
    this.response_Jpanel = new JPanel();
    this.response_Jpanel.setLayout(gbl);
    this.resp_in_textArea = new JTextArea();
    this.resp_out_textArea = new JTextArea();
    this.resp_encode_JscrollPane = new JScrollPane(this.resp_in_textArea);
    this.resp_decode_JscrollPane = new JScrollPane(this.resp_out_textArea);
    this.resp_encode_JscrollPane.setBorder(new TitledBorder("响应加密流量"));
    this.resp_decode_JscrollPane.setBorder(new TitledBorder("响应解密流量"));
    this.resp_decode_button = new JButton("解密响应流量");
    this.resp_decode_button.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            try {
              String payloadName = (String)GodzillaPanel.this.payload_comboBox.getSelectedItem();
              String cryptionName = (String)GodzillaPanel.this.crypt_comboBox.getSelectedItem();
              String key = GodzillaPanel.this.key_textField.getText().trim();
              String pass = GodzillaPanel.this.pass_textField.getText().trim();
              boolean isKeyEncrypted = GodzillaPanel.this.key_checkBox.isSelected();
              if (!isKeyEncrypted)
                key = functions.getSecretKeyX(key); 
              Cryption cryption = ApplicationContext.getCryption(payloadName, cryptionName);
              String respEncodeText = GodzillaPanel.this.resp_in_textArea.getText().trim();
              String respDecodeText = cryption.decodeResponse(respEncodeText.getBytes(StandardCharsets.UTF_8), pass, key);
              GodzillaPanel.this.resp_out_textArea.setText(respDecodeText);
            } catch (Exception e2) {
              e2.printStackTrace();
            } 
          }
        });
    addComponent(this.response_Jpanel, this.resp_encode_JscrollPane, gbl, gbc);
    gbc.weightx = 0.0D;
    gbc.weighty = 0.0D;
    addComponent(this.response_Jpanel, this.resp_decode_button, gbl, gbc);
    gbc.weightx = 1.0D;
    gbc.weighty = 1.0D;
    addComponent(this.response_Jpanel, this.resp_decode_JscrollPane, gbl, gbc);
    this.decode_tabbedPane.add(this.response_Jpanel, "响应数据解密");
  }
  
  private void addToComboBox(JComboBox<String> comboBox, String[] data) {
    for (String str : data)
      comboBox.addItem(str); 
  }
  
  private void addComponent(Container container, Component c, GridBagLayout gridBagLayout, GridBagConstraints gridBagConstraints) {
    gridBagLayout.setConstraints(c, gridBagConstraints);
    container.add(c);
  }
}
