'use client'
import dynamic from 'next/dynamic'

const PageMain = dynamic(() => import('../../components/pageMain'), { ssr: false })
export default function screenMain() {
  return (
    <PageMain />
  )
}