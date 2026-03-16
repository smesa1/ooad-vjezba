using LinearAlgebra
using Printf

# ===============================
# Jacobijev algoritam
# ===============================
function jacobi_method(A; tol=1e-10, max_iters=1000)
    A = copy(A)
    n = size(A, 1)
    Q = Matrix{Float64}(I, n, n)
    iter = 0

    while iter < max_iters
        max_val = 0.0
        p, q = 1, 2

        for i in 1:n-1
            for j in i+1:n
                if abs(A[i, j]) > max_val
                    max_val = abs(A[i, j])
                    p, q = i, j
                end
            end
        end

        if max_val < tol
            break
        end

        # stabilniji izraz za ugao
        θ = 0.5 * atan(2*A[p,q], A[q,q] - A[p,p])
        c = cos(θ)
        s = sin(θ)

        J = Matrix{Float64}(I, n, n)
        J[p,p] = c
        J[q,q] = c
        J[p,q] = s
        J[q,p] = -s

        A = transpose(J) * A * J
        Q = Q * J

        iter += 1
    end

    return diag(A), Q, iter
end

# ===============================
# NUMERIČKI PRIMJER IZ RADA
# ===============================

A = [
    1.0  sqrt(2)  2.0;
    sqrt(2)  3.0  sqrt(2);
    2.0  sqrt(2)  1.0
]

println("Originalna matrica A:")
println(A)

λ, V, iters = jacobi_method(A)

println("\nBroj iteracija: ", iters)

println("\nSopstvene vrijednosti (Jacobi):")
println(λ)

println("\nSopstveni vektori (kolone matrice V):")
println(V)

# ===============================
# PROVJERA TAČNOSTI
# Av = λv
# ===============================

println("\nProvjera Av = λv:")

for i in 1:3
    v = V[:, i]
    lhs = A * v
    rhs = λ[i] * v
    println("\nVektor v$i:")
    println("A * v$i  = ", lhs)
    println("λ$i * v$i = ", rhs)
    println("Razlika = ", lhs - rhs)
end

# ===============================
# POREĐENJE SA UGRAĐENOM FUNKCIJOM
# ===============================

println("\nPoređenje sa eigen(A):")
E = eigen(A)
println("Sopstvene vrijednosti (Julia):")
println(E.values)
