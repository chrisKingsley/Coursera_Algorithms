# Plot runtime data from Quick Find UF
qf = read.delim("runtime_QF.txt", header=F)
colnames(qf) = c("N","time")

plot(x=qf$N, y=qf$time,
     pch=20,
     cex=0.5,
     main="Quick Find",
     xlab="N",
     ylab="time")

f <- function(x) { x^4 }
c = qf$time[nrow(qf)]/f(qf$N[nrow(qf)])
curve(c*f(x), qf$N[1], qf$N[nrow(qf)], col="red", add=T)
legend("topleft", fill="red", legend=as.character(body(f)[2]))


# Plot runtime data from Weighted Quick Union UF
wqu = read.delim("runtime_wquf_50.txt", header=F)
colnames(wqu) = c("N","time")

x11()
plot(x=wqu$N, y=wqu$time,
     pch=20,
     cex=0.5,
     main="Weighted Quick Union",
     xlab="N",
     ylab="time")

f <- function(x) { x^2*log(x) }
c = wqu$time[nrow(wqu)]/f(wqu$N[nrow(wqu)])
curve(c*f(x), wqu$N[1], wqu$N[nrow(wqu)], col="red", add=T)
legend("topleft", fill="red", legend=as.character(body(f)[2]))
