package org.ui;

import com.core.ApplicationContext;
import java.awt.Font;
import javax.swing.*;

public class StartGUi extends JFrame {
  private static StartGUi startGUi;
  
  private JTabbedPane goziila_tabbedPane;
  
  public StartGUi() {
    try {
      initVariable();
    } catch (UnsupportedLookAndFeelException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } 
  }
  
  private void initVariable() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    setTitle("哥斯拉流量解密");
    try {
      UIManager.setLookAndFeel("com.apple.laf.AquaLookAndFeel");
    } catch (Exception e1) {
      // 记录日志（而不是打印堆栈）
      try {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
      } catch (Exception e2) {
        try {
          UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e3) {
          // 什么都不做，Metal 肯定可用
        }
      }
    }
    SwingUtilities.updateComponentTreeUI(this);
    setSize(1200, 800);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    System.setProperty("file.encoding", "UTF-8");
    Font globalFont = new Font("Menlo", Font.PLAIN, 15);
    UIManager.put("Label.font", globalFont);
    UIManager.put("CheckBox.font", globalFont);
    UIManager.put("Button.font", globalFont);
    UIManager.put("ComboBox.font", globalFont);
    UIManager.put("TabbedPane.font", globalFont);
    UIManager.put("TextArea.font", globalFont);
    UIManager.put("TextField.font", globalFont);
    GodzillaPanel godzillaPanel = new GodzillaPanel();
    ImageIcon weixin_imageIcon = new ImageIcon(getClass().getResource("/qrcode.jpg"));
    JLabel weixin_jLabel = new JLabel(weixin_imageIcon);
    JPanel TestjPanel = new JPanel();
    TestjPanel.add(weixin_jLabel);
    this.goziila_tabbedPane = new JTabbedPane();
    this.goziila_tabbedPane.add(godzillaPanel, "哥斯拉流量解密");
    this.goziila_tabbedPane.add(TestjPanel, "https://github.com/noculture/Deco_Godzilla");
    add(this.goziila_tabbedPane);
    setVisible(true);
  }
  
  public static void main(String[] args) {
    ApplicationContext.init();
    startGUi = new StartGUi();
  }
}
