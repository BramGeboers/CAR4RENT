import { useRouter } from 'next/router'
import React from 'react'
import { sessionStorageService } from '@/services/sessionStorageService'

const index = () => {
    const router = useRouter()
    const { message, email, role, id, token } = router.query
    if (typeof window === 'undefined') return <div>Redirecting</div>
    if (!email && !role && !token && !id && !message) {
        return <div>Redirecting</div>
    }
    if (!message) {
        sessionStorageService.setItem('email', email as string)
        sessionStorageService.setItem('role', role as string)
        sessionStorageService.setItem('token', token as string)

        router.push('/')
        // window.location.href = '/'
    }
    else {
        alert(message)
        sessionStorageService.clear()
        router.push('/login')
        // window.location.href = '/'
    }
    return <div>Redirecting</div>
}

export default index
