package da

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

import models.Payment
import play.api.db.Database
import anorm._

class PaymentDao @Inject()(db: Database) {

  def findAll(): List[Payment] = {
    db.withConnection {
      implicit c => {
        SQL(
          """
            SELECT * FROM payment
          """).as(Payment.simple *)
      }
    }
  }

  def insert(payment: Payment) = {
    db.withConnection { implicit c =>
      SQL(
        """
            INSERT INTO Payment(payTimestamp, userId, comment, paymentType, amount, paypackId, status, balance)
            VALUES ({payTimestamp}, {userId}, {comment}, {paymentType}, {amount}, {paypackId}, {status}, {balance})
          """
      ).on(
        "payTimestamp" -> payment.timestamp,
        "userId" -> payment.userId,
        "comment" -> payment.comment,
        "paymentType" -> payment.paymentType.toString,
        "amount" -> payment.amount,
        "paypackId" -> payment.paypackId,
        "status" -> payment.status.toString,
        "balance" -> payment.balance
      ).executeInsert()
    }
  }
}
