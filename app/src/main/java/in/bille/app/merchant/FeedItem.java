package in.bille.app.merchant;

/**
 * Created by SHUBHAM on 05-11-2015.
 */
public class FeedItem {

    private String customerName;
    private String billAmount;
    private String thumbnail;
    private String c_name;
    private String c_phone;
    private String total;
    private String totalBill;
    private String qty;
    private String date;
    private String bill_id;
    private String menu_id;

    public String getTitle() {
        return customerName;
    }

    public void setTitle(String title) {
        this.customerName = title;
    }

    public String getPrice() {
        return billAmount;
    }

    public void setPrice(String price) {
        this.billAmount = price;
    }


    public String getMenuId() {
        return menu_id;
    }

    public void setMenuId(String price) {
        this.menu_id = price;
    }



    public String getBillId() {
        return bill_id;
    }

    public void setBillId(String price) {
        this.bill_id = price;
    }


    public String getName() {
        return c_name;
    }

    public void setName(String cName) {
        this.c_name = cName;
    }

    public String getPhone() {
        return c_phone;
    }

    public void setPhone(String cPhone) {
        this.c_phone = cPhone;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(String totalBill) {
        this.totalBill = totalBill;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
