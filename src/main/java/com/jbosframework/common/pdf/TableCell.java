package com.jbosframework.common.pdf;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import java.io.IOException;
/**
 * TableCell
 * @author youfu.wang
 * @date 2019-07-01
 */
public class TableCell {
    private String content;
    private BaseColor backgroundColor=BaseColor.WHITE;
    private int horizontalAlignment= Element.ALIGN_LEFT;
    private int rowspan;
    private int colspan;

    public TableCell(String content){
       this.content=content;
    }
    public TableCell(String content,BaseColor backgroundColor){
        this.content=content;
        this.backgroundColor=backgroundColor;
    }
    public TableCell(String content,int horizontalAlignment){
        this.content=content;
        this.horizontalAlignment=horizontalAlignment;
    }
    public TableCell(String content,BaseColor backgroundColor,int rowspan,int colspan){
        this.content=content;
        this.backgroundColor=backgroundColor;
        this.rowspan=rowspan;
        this.colspan=colspan;
    }
    public TableCell(String content,BaseColor backgroundColor,int horizontalAlignment,int rowspan,int colspan){
        this.content=content;
        this.backgroundColor=backgroundColor;
        this.horizontalAlignment=horizontalAlignment;
        this.rowspan=rowspan;
        this.colspan=colspan;
    }
    public PdfPCell newCell() throws IOException, DocumentException {
        Paragraph paragraph=new Paragraph(content,ContentFont.getZhFont());
        PdfPCell cell = new PdfPCell(paragraph);
        cell.setMinimumHeight(paragraph.getFont().getSize()+5);
        cell.setBackgroundColor(backgroundColor);
        cell.setHorizontalAlignment(horizontalAlignment);
        if(rowspan>0){
            cell.setRowspan(rowspan);
        }
        if(colspan>0){
            cell.setColspan(colspan);
        }
        return cell;
    }
}
