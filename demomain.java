//package EditorTools;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.*;
import java.nio.file.*;
import java.nio.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList; 

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;//StyledDocument
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import EditorTools.*;
import EditorTools.FindReplace;

public class demomain{
    private JFrame jf = new JFrame("test");
    private JTextPane t = new JTextPane();
    private   JScrollPane scrollPane = new JScrollPane(t,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );   //show the scrollbar as needed

    private StringBuilder  caching_text = new StringBuilder("");   //save the last condition of text_area 

    private int file_number = 0;     //the templete_file name number
    private StringBuilder file_name = new StringBuilder("");  //save the opening file_name

    //the compination of menubar
    private JMenuBar menubar = new JMenuBar();
    private JMenu file = new JMenu("文件(F)");
    private JMenu binary = new JMenu("二进制文件(B)");
    private JMenu edit = new JMenu("编辑(E)");
    private JMenu view = new JMenu("视图(E)");
    private JMenu about = new JMenu("帮助(H)");
    private JMenuItem openbinaryMenuItem = new JMenuItem("打开");
    private JMenuItem savebinaryMenuItem = new JMenuItem("保存");
    private JMenuItem insertMenuItem = new JMenuItem("插入时间");
    private JMenuItem buildMenuItem = new JMenuItem("新建");
    private JMenuItem openMenuItem = new JMenuItem("打开");
    private JMenuItem saveMenuItem = new JMenuItem("保存");
    private JMenuItem exitMenuItem = new JMenuItem("退出");
    private JMenuItem copyMenuItem = new JMenuItem("复制");
    private JMenuItem pasteMenuItem = new JMenuItem("粘贴");
    //private JMenuItem findMenuItem = new JMenuItem("查找/替换");

    //the find and replace bar at the bottom of the jframe
    private JPanel p = new JPanel(new BorderLayout());
    private JPanel q = new JPanel(new FlowLayout());
    private JTextField find_field = new JTextField(23);
    private JButton find_btn = new JButton("查找");
    private JTextField replace_field = new JTextField(23);
    private JButton replace_btn = new JButton("替换");
    private JButton cancel_btn = new JButton("取消");


/***************************** inite ****************************************************/
    public demomain(){
        jf.setSize(800, 600);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

/****************************set JTextPane**********************************************/
        t.setFont(new Font("", Font.BOLD, 16));   //set font size
        t.getDocument().addDocumentListener(new SyntaxHighlighter(t));// set SyntaxHighlight
        

/********************** set editarea******************************************************/
        scrollPane.setWheelScrollingEnabled(true);   //answer the event of mouse_wheel

/********************************set menu*************************************************/
        menubar.add(file);
        menubar.add(binary);
        menubar.add(edit);
        menubar.add(view);
        menubar.add(about);

        file.add(buildMenuItem);
        file.add(openMenuItem);
        file.add(saveMenuItem);
        file.addSeparator();       // 添加一条分割线
        file.add(exitMenuItem);

        binary.add(openbinaryMenuItem);
        binary.add(savebinaryMenuItem);
           // 子菜单添加到一级菜单
        edit.add(copyMenuItem);
        edit.add(pasteMenuItem);
        edit.addSeparator();
        edit.add(insertMenuItem);
        //edit.add(findMenuItem);


        q.add(find_field);
        q.add(find_btn);
        q.add(cancel_btn);
        q.add(replace_field);
        q.add(replace_btn);
        

        p.add(q, BorderLayout.SOUTH);
        jf.add(p);
        jf.setContentPane(p);
        jf.setLocationRelativeTo(null);


            
        jf.setJMenuBar(menubar);   //add the menubar
        jf.add(scrollPane);         //add the scrollpane
        jf.setVisible(true);        //set unvisible



        openMenuItem.addActionListener(new openListener());
        
        saveMenuItem.addActionListener(new saveListener());

        buildMenuItem.addActionListener(new newListener());

        find_btn.addActionListener(new findListener());

        cancel_btn.addActionListener(new cancelListener());

        replace_btn.addActionListener(new replaceListener());

        insertMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder temp = new StringBuilder(t.getText());
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String tablename=dateFormat.format(now);
                temp.append(tablename);
                t.setText(temp.toString());

            }
        });

        openbinaryMenuItem.addActionListener(new openbinaryListener());

        savebinaryMenuItem.addActionListener(new savebinaryListener());
    
    }

    class openbinaryListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{
                openbinary(jf, t);
            }
            catch(Exception k){
                System.out.println("openB Wrong");
            }
        }
    }

    class savebinaryListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{
                savebinary(jf, t);
            }
            catch(Exception k){
                System.out.println("openB Wrong");
            }
        }
    }

    class findListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{
                if(find_field.getText().compareTo("") != 0)
                    new FindReplace().highlight(t, find_field.getText());
            }       
            catch(Exception k){
                System.out.println("find Wrong");
            }
        }
    }

    class cancelListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{
                new FindReplace().removeHighlights(t);
                find_field.setText("");
            }
            catch(Exception k){
                System.out.println("cancel Wrong");
            }
        }
    }
    
    class replaceListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{
                new FindReplace().replace(t, find_field.getText(), replace_field.getText());

            }
            catch(Exception k){
                System.out.println("replace Wrong");
            }
        }
    }

    class openListener implements ActionListener{ 
            //define the action handler
            public void actionPerformed(ActionEvent e) {
                try{
                open(jf, t);
            }
            catch(Exception k){
                System.out.println("open Wrong");
            }
            }
        };


    class newListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{
                build(jf, t);
            }
            catch(Exception k){
                System.out.println("build Wrong");

            }
        }
    };


    class saveListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{
                save(jf, t);
            }
            catch(Exception k){
                System.out.println("save Wrong");

            }
        }
    };



    public void Update_CachingText(JTextPane msgTextArea){
        caching_text.replace(0, caching_text.length(), msgTextArea.getText());
    }


    public void Clarify_Text(JTextPane msgTextArea, StringBuilder fn){
        msgTextArea.setText("");
        fn.replace(0, fn.length(), "");
        Update_CachingText(msgTextArea);
    }


    public void NullFile_SaveFuction(Component parent, JTextPane msgTextArea) throws IOException{
        //creat a default file selector
        JFileChooser fileChooser = new JFileChooser();
        // 设置默认显示的文件夹为当前文件夹
        fileChooser.setCurrentDirectory(new File("/"));

        String temp_file_name = "New_TextFile(" + Integer.toString(file_number) + ").txt";
        file_number++;
        // 设置打开文件选择框后默认输入的文件名
        fileChooser.setSelectedFile(new File(temp_file_name));

        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = fileChooser.showSaveDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            //abtain the file has been established
            File file = fileChooser.getSelectedFile();
            file_name.replace(0, file_name.length(), file.getName()); 
            //write the content in Jtextpane (t.getText()) to file
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
             // 写入信息
            try{
                bufferedWriter.write(msgTextArea.getText());
                bufferedWriter.flush();// 清空缓冲区
            }catch (IOException x){
                System.err.format("IOException: %s%n", x);
            }finally{
                if(bufferedWriter != null)
                bufferedWriter.close();// 关闭输出流
            }

            Update_CachingText(msgTextArea);
        }
    }


    public void File_SaveFuction(Component parent, JTextPane msgTextArea) throws IOException{
        File file = new File("/"+ file_name.toString());
        //System.out.println(file.getName());
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
             // 写入信息
            try{
                bufferedWriter.write(msgTextArea.getText());
                bufferedWriter.flush();// 清空缓冲区
            }catch (IOException x){
                System.err.format("IOException: %s%n", x);
            }finally{
                if(bufferedWriter != null)
                bufferedWriter.close();// 关闭输出流
            }
        //Update_CachingText(msgTextArea);
    }

    public void savebinary(Component parent, JTextPane msgTextArea) throws IOException{
        File file = new File("/"+ file_name.toString());
        //System.out.println(file.getName());
        OutputStream outputStream = new  FileOutputStream(file);
             // 写入信息
            try{
                String temp = new String(msgTextArea.getText().toString());     
                String str[] = temp.split("  ");       //str.length() si the count of word in the file

                byte[] allBytes = new byte[str.length];

                for(int i = 0; i<str.length; i++){
                    int k = Integer.parseInt(str[i]);
                    allBytes[i] = (byte)k;
                }

                outputStream.write(allBytes);
 
            }catch (IOException x){
                System.err.format("IOException: %s%n", x);
            }finally{
                if(outputStream != null)
                outputStream.close();// 关闭输出流
            }
        JOptionPane.showMessageDialog(parent,"save successfully","",JOptionPane.INFORMATION_MESSAGE);

    }




    public void File_ReadFunction(Component parent, JTextPane msgTextArea) throws IOException{
        JFileChooser fileChooser = new JFileChooser();
        // 设置默认显示的文件夹为当前文件夹
        fileChooser.setCurrentDirectory(new File("/"));

        // 设置文件选择的模式（只选文件、只选文件夹、文件和文件均可选）
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）`
        int result = fileChooser.showOpenDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"确定", 则获取选择的文件路径

            //set the default file is empty
            //but if a file  was chose, and you click the approve_option, it will change to the file you chose. 
            File file = fileChooser.getSelectedFile();  
            file_name.replace(0, file_name.length(), file.getName());  
            

            FileReader reader = new FileReader(file);//定义一个fileReader对象，用来初始化BufferedReader
            BufferedReader bReader = new BufferedReader(reader);//new一个BufferedReader对象，将文件内容读取到缓存
            StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
            String s = "";

            try{
            while ((s =bReader.readLine()) != null) //逐行读取文件内容，不读取换行符和末尾的空格
                sb.append(s + "\n");//将读取的字符串添加换行符后累加存放在缓存中
            }
            catch(IOException x){
                System.err.format("IOException: %s%n", x);
            }finally{
                if(bReader != null)
                    bReader.close();
            }
            msgTextArea.setText(sb.toString());
            Update_CachingText(msgTextArea);
        }

    }


      public void openbinary(Component parent, JTextPane msgTextArea) throws IOException{
        JFileChooser fileChooser = new JFileChooser();
        // 设置默认显示的文件夹为当前文件夹
        fileChooser.setCurrentDirectory(new File("/"));

        // 设置文件选择的模式（只选文件、只选文件夹、文件和文件均可选）
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）`
        int result = fileChooser.showOpenDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();  
            file_name.replace(0, file_name.length(), file.getName());  
            
            StringBuilder temp = new StringBuilder("");
        try{
            InputStream inputStream = new  FileInputStream(file);
            //long size = file.length();
            byte[] all = new byte[(int)file.length()];
        
            int size = inputStream.read(all);
            for(int i = 0; i<size; i++){
                temp.append(all[i]);
                temp.append("  ");
            }

            
        }catch (IOException ex){
            System.err.format("IOException: %s%n", ex);
        }

            msgTextArea.setText(temp.toString());
            Update_CachingText(msgTextArea);
        }
    }


    public void build(Component parent, JTextPane msgTextArea) throws IOException{
        if(t.getText().compareTo(caching_text.toString()) != 0){
            int option_selected = JOptionPane.showConfirmDialog(parent, "是否保存?", "提示", JOptionPane.YES_NO_CANCEL_OPTION);
            if(option_selected == 0){
                if(file_name.toString().compareTo("") == 0){
                    NullFile_SaveFuction(parent, msgTextArea);

                }
                else{
                    File_SaveFuction(parent, msgTextArea);
                    Update_CachingText(msgTextArea);
                }
                Clarify_Text(msgTextArea, file_name);
            }
            if(option_selected == 1){
                Clarify_Text(msgTextArea, file_name);
            }
        }    
        else{
            Clarify_Text(msgTextArea, file_name);
        }    

    }



    public void save(Component parent, JTextPane msgTextArea) throws IOException{
        if(file_name.toString().compareTo("") == 0){
            NullFile_SaveFuction(parent, msgTextArea);
        }
        else{
            File_SaveFuction(parent, msgTextArea);
            JOptionPane.showMessageDialog(parent,"save successfully","",JOptionPane.INFORMATION_MESSAGE);
            }

        Update_CachingText(msgTextArea);
                
    }


  

    public  void open(Component parent, JTextPane msgTextArea) throws IOException{
        if(t.getText().compareTo(caching_text.toString()) != 0 ){ 

            int option_selected = JOptionPane.showConfirmDialog(parent, "是否保存?", "提示", JOptionPane.YES_NO_CANCEL_OPTION);
            if(option_selected == 0){
                if(file_name.toString().compareTo("") == 0){
                    NullFile_SaveFuction(parent, msgTextArea);

                }
                else{
                    File_SaveFuction(parent, msgTextArea);
                    Update_CachingText(msgTextArea);
                }
                File_ReadFunction(parent, msgTextArea);
            }
            if(option_selected == 1){
                File_ReadFunction(parent, msgTextArea);
            }
            
        }
        else{
            File_ReadFunction(parent, msgTextArea);

        }

    }

    public static void main(String[] args){
        new demomain();

    }

}




