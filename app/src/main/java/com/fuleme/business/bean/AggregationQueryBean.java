package com.fuleme.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/6.
 */

public class AggregationQueryBean {


    /**
     * error_code : 200
     * message : 请求成功
     * data : {"orders":[{"number":4,"amount":"43.00"}],"refund":[{"number":0,"amount":"0.00"}],"offers":[{"number":1,"amount":"0.50"}],"counter_fee":[{"number":1,"amount":"0.50"}],"paid":[{"number":4,"amount":"43.00"}]}
     */

    private String error_code;
    private String message;
    private DataBean data;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<OrdersBean> orders;
        private List<RefundBean> refund;
        private List<OffersBean> offers;
        private List<CounterFeeBean> counter_fee;
        private List<PaidBean> paid;

        public List<OrdersBean> getOrders() {
            return orders;
        }

        public void setOrders(List<OrdersBean> orders) {
            this.orders = orders;
        }

        public List<RefundBean> getRefund() {
            return refund;
        }

        public void setRefund(List<RefundBean> refund) {
            this.refund = refund;
        }

        public List<OffersBean> getOffers() {
            return offers;
        }

        public void setOffers(List<OffersBean> offers) {
            this.offers = offers;
        }

        public List<CounterFeeBean> getCounter_fee() {
            return counter_fee;
        }

        public void setCounter_fee(List<CounterFeeBean> counter_fee) {
            this.counter_fee = counter_fee;
        }

        public List<PaidBean> getPaid() {
            return paid;
        }

        public void setPaid(List<PaidBean> paid) {
            this.paid = paid;
        }

        public static class OrdersBean {
            /**
             * number : 4
             * amount : 43.00
             */

            private int number;
            private String total_fee;

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            public String getAmount() {
                return total_fee;
            }

            public void setAmount(String amount) {
                this.total_fee = amount;
            }
        }

        public static class RefundBean {
            /**
             * number : 0
             * amount : 0.00
             */

            private int number;
            private String total_fee;

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            public String getAmount() {
                return total_fee;
            }

            public void setAmount(String amount) {
                this.total_fee = amount;
            }
        }

        public static class OffersBean {
            /**
             * number : 1
             * amount : 0.50
             */

            private int number;
            private String total_fee;

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            public String getAmount() {
                return total_fee;
            }

            public void setAmount(String amount) {
                this.total_fee = amount;
            }
        }

        public static class CounterFeeBean {
            /**
             * number : 1
             * amount : 0.50
             */

            private int number;
            private String total_fee;

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            public String getAmount() {
                return total_fee;
            }

            public void setAmount(String amount) {
                this.total_fee = amount;
            }
        }

        public static class PaidBean {
            /**
             * number : 4
             * amount : 43.00
             */

            private int number;
            private String total_fee;

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            public String getAmount() {
                return total_fee;
            }

            public void setAmount(String amount) {
                this.total_fee = amount;
            }
        }
    }
}
