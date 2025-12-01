import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import org.apache.log4j.Logger;

public class PDFReport {
    private static final Logger log = Logger.getLogger(PDFReport.class);

    public static File chooseSaveLocation(JFrame parent, File downloadsDir) {
        JFileChooser chooser = new JFileChooser(downloadsDir);
        chooser.setDialogTitle("Сохранить PDF отчёт");
        chooser.setFileFilter(new FileNameExtensionFilter("PDF (*.pdf)", "pdf"));

        if (chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION)
            return null;

        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".pdf"))
            file = new File(file.getParentFile(), file.getName() + ".pdf");

        return file;
    }

    public static void generate(List<String> groups, File file) throws Exception {
        if (file == null) return;

        log.info("Начата генерация PDF отчета: " + file.getAbsolutePath());
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);

        InputStream fontStream = PDFReport.class.getResourceAsStream("/fonts/FreeSans.ttf");
        if (fontStream == null) {
            log.error("Шрифт FreeSans.ttf не найден");
            throw new RuntimeException("Шрифт FreeSans.ttf не найден в resources/fonts/");
        }

        PDType0Font font = PDType0Font.load(doc, fontStream);
        PDPageContentStream cs = new PDPageContentStream(doc, page);

        // Заголовок и список групп
        cs.setNonStrokingColor(new Color(230, 240, 255));
        cs.addRect(0, 740, page.getMediaBox().getWidth(), 50);
        cs.fill();

        cs.beginText();
        cs.setFont(font, 20);
        cs.setNonStrokingColor(Color.BLACK);
        float titleWidth = font.getStringWidth("Отчет: музыкальные группы") / 1000 * 20;
        float xCentered = (page.getMediaBox().getWidth() - titleWidth) / 2;
        cs.newLineAtOffset(xCentered, 760);
        cs.showText("Отчет: музыкальные группы");
        cs.endText();

        cs.setStrokingColor(new Color(120, 140, 180));
        cs.setLineWidth(2);
        cs.moveTo(40, 735);
        cs.lineTo(page.getMediaBox().getWidth() - 40, 735);
        cs.stroke();

        cs.setFont(font, 14);
        cs.setNonStrokingColor(Color.BLACK);
        int y = 700;
        for (String group : groups) {
            cs.beginText();
            cs.newLineAtOffset(60, y);
            cs.showText("• " + group);
            cs.endText();
            y -= 22;
        }

        cs.close();
        doc.save(file);
        doc.close();
        log.info("PDF отчет успешно создан: " + file.getAbsolutePath());
    }

    public static void generateReportDialog(JFrame parent, List<String> groups, File downloadsFolder) {
        try {
            File file = chooseSaveLocation(parent, downloadsFolder);
            if (file != null) {
                generate(groups, file);
                JOptionPane.showMessageDialog(parent, "PDF отчёт успешно создан!");
            }
        } catch (Exception e) {
            log.error("Ошибка при создании PDF отчета", e);
            JOptionPane.showMessageDialog(parent,
                    "Ошибка при создании отчёта: " + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
