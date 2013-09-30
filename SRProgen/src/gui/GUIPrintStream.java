package gui;

import java.io.OutputStream;
import java.io.PrintStream;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;


public class GUIPrintStream extends PrintStream{
   
    private Text text;
   
    public GUIPrintStream(OutputStream out, Text text){
        super(out);
        this.text = text;
    }
   
    /** *//**
     * 重写write()方法，将输出信息填充到GUI组件
     * @param buf
     * @param off
     * @param len
     */
    @Override
//    public void write(byte[] buf, int off, int len) {
//        final String message = new String(buf, off, len);
//        sb.append(message);
//        component.setText(sb.toString());
////        component.setCaretPosition(component.getText().length()); 设置光标位置
//    }
    
//    /**
//    * 在这里重截,所有的打印方法都要调用的方法
//    */
//        public void write(byte[] buf, int off, int len) {
//            final String message = new String(buf, off, len);
//           
//            /**//* SWT非界面线程访问组件的方式 */
//           Display.getDefault().syncExec(new Thread(){
//               public void run(){
//                    /**//* 在这里把信息添加到组件中 */
//                   text.append(message);
//                }
//           });
//        }
    
	public void write(byte[] buf, int off, int len) {
		final String message = new String(buf, off, len);
		if (text != null && !text.isDisposed()) {
			/**//* SWT非界面线程访问组件的方式 */
			Display.getDefault().syncExec(new Thread() {
				public void run() {
					/**//* 在这里把信息添加到组件中 */
					text.append(message);
				}
			});
		} else {
			super.write(buf, off, len);
		}
	}
  
}